package com.gavkariapp.activity

import android.app.AlertDialog
import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.MySaleAdAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.HttpConstant.SUCCESS
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_my_sale_ad.*
import kotlinx.android.synthetic.main.layout_no_data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MySaleAdActivity : BaseActivity(), MySaleAdAdapter.OnItemClickListener {

    private lateinit var mySaleAdResponse: MySaleAdResponse

    lateinit var mySaleAdList: ArrayList<MySaleAd>

    lateinit var mySaleAdAdapter: MySaleAdAdapter

    lateinit var homeViewModel: HomeViewModel

    lateinit var observer: Observer<ApiResponse<MySaleAdResponse, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_my_sale_ad)
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_sale_ads))
        setupView()
    }

    override fun onResume() {
        super.onResume()
        accessData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onItemClick(mySaleAd: MySaleAd) {

        var buySale = BuySale(mySaleAd.id,mySaleAd.user_id,mySaleAd.village_id,mySaleAd.status,mySaleAd.tab_type,
                mySaleAd.type,mySaleAd.name,mySaleAd.price,mySaleAd.breed,mySaleAd.pregnancies_count,mySaleAd.pregnancy_status,
                mySaleAd.milk,mySaleAd.weight,mySaleAd.company,mySaleAd.model,mySaleAd.year,mySaleAd.km_driven,mySaleAd.power,
                mySaleAd.capacity,mySaleAd.material,mySaleAd.tynes_count,mySaleAd.size,mySaleAd.phase,mySaleAd.latitude,
                mySaleAd.longitude,mySaleAd.village_en,mySaleAd.village_mr,mySaleAd.created_at,mySaleAd.photo,mySaleAd.title,
                mySaleAd.description,mySaleAd.fav_user_id,0.0,"MySaleAdActivity")

        startActivity(Intent(applicationContext,BuySaleDetailActivity::class.java).putExtra("buySaleData",buySale))
    }

    override fun onClickDelete(mySaleAd: MySaleAd) {
        showDeleteAlert(mySaleAd)
    }

    override fun onClickEdit(mySaleAd: MySaleAd) {
        startActivity(Intent(this,EditSaleAdActivity::class.java)
                .putExtra("mySaleAd",mySaleAd))
    }

    override fun onClickSold(mySaleAd: MySaleAd) {
        showSaleAlert(mySaleAd)
    }

    private fun setupView() {
        homeViewModel = HomeViewModel()
        mySaleAdList = ArrayList<MySaleAd>()
        initObserver()
        rvMySaleAd.applyVerticalWithDividerLinearLayoutManager()
    }

    private fun initObserver() {
        observer = Observer { t ->
            if (t?.response != null) {
                mySaleAdResponse = t.response!!
                if (mySaleAdResponse?.status == SUCCESS) {
                    displayData(mySaleAdResponse)
                } else {
                    nvMySaleAd.visibility = View.GONE
                    layNoDataMySaleAd.visibility = View.VISIBLE
                    layNoInternetMySaleAd.visibility = View.GONE
                    layNoDataText.text = getString(R.string.msg_create_ad)
                    dismissProgress()
                }

            } else {
                showError(t?.error!!)
                dismissProgress()
            }
        }
    }

    private fun accessData(){
        showProgress()
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userId: String? = prefs[ApiConstant.USER_ID, "-1"]
        if (InternetUtil.isInternetOn()) {
            homeViewModel.accessMySaleAdData(userId!!).observe(this, observer)
        } else {
            dismissProgress()
            waitForInternetConnection()
        }
    }

    private fun displayData(response: MySaleAdResponse?) {
        mySaleAdList = response!!.MySaleAds
        mySaleAdAdapter = MySaleAdAdapter(mySaleAdList, this)
        nvMySaleAd.visibility = View.VISIBLE
        layNoDataMySaleAd.visibility = View.GONE
        rvMySaleAd.adapter = mySaleAdAdapter
        dismissProgress()
    }

    open fun deleteAd(item: MySaleAd) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
                .deleteMySaleAd(item.id)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {
                            runOnUiThread {
                                dismissProgress()
                                mySaleAdList.remove(item)
                                rvMySaleAd.adapter?.notifyDataSetChanged()
                                if (response.body()!!.message == "deleted") {
                                    showSuccess(getString(R.string.msg_delete_sale))
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

    open fun markSold(item: MySaleAd) {
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
                .markAsSold(item.id)
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {
                            runOnUiThread {
                                dismissProgress()
                                onResume()
                                /*if (response.body()!!.message == "sold") {
                                    showSuccess(getString(R.string.lbl_sale_complete))
                                }*/
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

    private fun showDeleteAlert(item: MySaleAd) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.msg_alert))
        builder.setMessage(getString(R.string.qes_delete_ad))
        builder.setPositiveButton(getString(R.string.msg_yes)) { dialog, which ->
            dialog.cancel()
            deleteAd(item)
        }
        builder.setNegativeButton(getString(R.string.msg_no)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun showSaleAlert(item: MySaleAd) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.msg_alert))
        builder.setMessage(getString(R.string.lbl_sale_alert))
        builder.setPositiveButton(getString(R.string.msg_yes)) { dialog, which ->
            dialog.cancel()
            markSold(item)
        }
        builder.setNegativeButton(getString(R.string.msg_no)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                nvMySaleAd.visibility = View.VISIBLE
                layNoInternetMySaleAd.visibility = View.GONE
                layNoDataMySaleAd.visibility = View.GONE

            } else {
                layNoInternetMySaleAd.visibility = View.VISIBLE
                nvMySaleAd.visibility = View.GONE
                layNoDataMySaleAd.visibility = View.GONE

            }
        })
    }
}

