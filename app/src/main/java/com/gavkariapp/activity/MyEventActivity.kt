package com.gavkariapp.activity

import android.app.AlertDialog
import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.view.View
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.MyEventAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant.OPEN_MY_AD_DETAIL_ACTIVITY
import com.gavkariapp.constant.HttpConstant.SUCCESS
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_my_ad.*
import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyEventActivity : BaseActivity(), MyEventAdapter.OnItemClickListener {

    private lateinit var myAdResponse: MyAdResponse

    lateinit var myEvent: ArrayList<MyEvent>

    lateinit var myAdAdapter: MyEventAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<MyAdResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_my_ad)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_my_ad))
        setupView()

    }

    override fun onResume() {
        super.onResume()
        //access data
        accessData()
    }

    override fun onItemClick(item: MyEvent) {
        startActivityForResult(Intent(applicationContext, EventDetailActivity::class.java)
                .putExtra("VillageResponse", item), OPEN_MY_AD_DETAIL_ACTIVITY)
    }

    override fun onPublishClick(item: MyEvent) {
        val createAdBody = CreateAdBody(item.user_id, item.village_id,item.type,item.title,
                item.subtitle,item.subtitle_one,item.subtitle_two,item.subtitle_three,item.subtitle_four,item.subtitle_five,
                item.family, item.description,item.description_one, item.event_date, item.event_date_ms,
                item.muhurt, item.event_media, item.address, item.location, item.latitude, item.longitude,
                item.contact_no, item.note,item.photo, item.id,item.event_aid, item.status)

        startActivity(Intent(applicationContext, CreateEventThreeActivity::class.java)
                    .putExtra("createAdBody", createAdBody))


    }

    override fun onEditClick(item: MyEvent) {
        val createAdBody = CreateAdBody(item.user_id, item.village_id,item.type,item.title,
                item.subtitle,item.subtitle_one,item.subtitle_two,item.subtitle_three,item.subtitle_four,item.subtitle_five,
                item.family, item.description,item.description_one, item.event_date, item.event_date_ms,
                item.muhurt, item.event_media, item.address, item.location, item.latitude, item.longitude,
                item.contact_no, item.note, item.photo, item.id,item.event_aid, item.status)

        startActivity(Intent(applicationContext, EditEventActivity::class.java)
                .putExtra("createAdBody", createAdBody))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        overridePendingTransition(0, 0)
        ActivityCompat.finishAffinity(this@MyEventActivity)

    }

    private fun setupView() {

        applyLocale(this)

        //init class
        homeViewModel = HomeViewModel()

        //init observer
        initObserver()

        //set rv
        rvMyAd.applyVerticalWithDividerLinearLayoutManager()


    }

    private fun initObserver() {
        observer = Observer { t ->

            if (t?.response != null) {

                myAdResponse = t.response!!

                if (myAdResponse?.status == SUCCESS) {
                    displayData(myAdResponse)
                } else {
                    //showError(myAdResponse!!.message)
                    nvMyAd.visibility = View.GONE
                    layNoDataMyad.visibility = View.VISIBLE
                    layNoInternetMyAd.visibility = View.GONE
                    layNoDataText.text=getString(R.string.msg_create_evnt_first)
                    dismissProgress()
                }

            } else {
                showError(t?.error!!)
                dismissProgress()
            }
        }
    }

    private fun accessData() {

        showProgress()

        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userId: String? = prefs[ApiConstant.USER_ID, "-1"]

        if (InternetUtil.isInternetOn()) {
            homeViewModel.accessMyAdsCall(userId!!).observe(this, observer)
        } else {
            dismissProgress()
            waitForInternetConnection()
        }
    }

    private fun displayData(response: MyAdResponse?) {
        myEvent = response!!.MyAd
        myAdAdapter = MyEventAdapter(myEvent, this)
        nvMyAd.visibility = View.VISIBLE
        layNoDataMyad.visibility = View.GONE
        rvMyAd.adapter = myAdAdapter
        dismissProgress()

    }

    open fun deleteEvent(item: MyEvent) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
                .deleteMyAd(item.id)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {
                            runOnUiThread {
                                dismissProgress()
                                myEvent.remove(item)
                                rvMyAd.adapter?.notifyDataSetChanged()
                                if (response.body()!!.message == "deleted") {
                                    showSuccess(getString(R.string.msg_delete_ad))
                                }
                            }
                        } else {
                            if (response.errorBody() != null) {
                                runOnUiThread {
                                    dismissProgress()
                                    showError(response.errorBody().toString())
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                        runOnUiThread {
                            dismissProgress()
                            showError(t.toString())
                        }
                    }
                })
    }

    override fun onDeleteAdClick(item: MyEvent) {
        showDeleteAlert(item)
    }

    override fun onShareClick(item: MyEvent) {
        var design = Design(item.type,item.title,item.family,item.muhurt,item.subtitle,
                item.subtitle_one,item.subtitle_two,item.subtitle_three,
                item.subtitle_four,item.subtitle_five,item.note,
                item.description,item.description_one,item.address,item.photo)

        startActivity(Intent(this, SingleDesignActivity::class.java).putExtra("event_obj", design))

    }

    private fun showDeleteAlert(item: MyEvent) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.msg_alert))
        builder.setMessage(getString(R.string.msg_delete_alert))
        builder.setPositiveButton(getString(R.string.msg_yes)) { dialog, which ->
            dialog.cancel()
            deleteEvent(item)
        }
        builder.setNegativeButton(getString(R.string.msg_no)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                nvMyAd.visibility = View.VISIBLE
                layNoInternetMyAd.visibility = View.GONE
                layNoDataMyad.visibility = View.GONE

            } else {
                layNoInternetMyAd.visibility = View.VISIBLE
                nvMyAd.visibility = View.GONE
                layNoDataMyad.visibility = View.GONE

            }
        })
    }
}

