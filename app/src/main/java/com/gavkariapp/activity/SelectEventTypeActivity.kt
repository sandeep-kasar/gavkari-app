package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gavkariapp.Model.Matter
import com.gavkariapp.Model.ResponseEventMatter
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant.BIRTHDAY
import com.gavkariapp.constant.AppConstant.DASHKRIYA_VIDHI
import com.gavkariapp.constant.AppConstant.ENGAGEMENT
import com.gavkariapp.constant.AppConstant.FIRST_MEMORIAL
import com.gavkariapp.constant.AppConstant.HOUSE_WARMING
import com.gavkariapp.constant.AppConstant.JAGARAN_GONDHAL
import com.gavkariapp.constant.AppConstant.MAHAPRASAD
import com.gavkariapp.constant.AppConstant.OTHER_EVENT
import com.gavkariapp.constant.AppConstant.RETIREMENT
import com.gavkariapp.constant.AppConstant.SATYANARAYAN_POOJA
import com.gavkariapp.constant.AppConstant.WEDDING
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import kotlinx.android.synthetic.main.activity_event_select.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectEventTypeActivity : BaseActivity(), View.OnClickListener {

    var matter = ArrayList<Matter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_event_select)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_create_ad))
        tvWedding.setOnClickListener(this)
        tvEngagment.setOnClickListener(this)
        tvhouseWarm.setOnClickListener(this)
        tvjagran.setOnClickListener(this)
        tvDashkriya.setOnClickListener(this)
        tvVarshShradh.setOnClickListener(this)
        tvRetirement.setOnClickListener(this)
        tvbirthday.setOnClickListener(this)
        tvSatyaNarayan.setOnClickListener(this)
        tvMahaPrasad.setOnClickListener(this)
        tvOtherEvent.setOnClickListener(this)
        loadData()
    }
    override fun onClick(v: View?) {

        when (v) {
            tvWedding->{openCreateAdOneActivity(WEDDING)}
            tvEngagment->{openCreateAdOneActivity(ENGAGEMENT)}
            tvVarshShradh->{openCreateAdOneActivity(FIRST_MEMORIAL)}
            tvhouseWarm->{openCreateAdOneActivity(HOUSE_WARMING)}
            tvDashkriya->{openCreateAdOneActivity(DASHKRIYA_VIDHI)}
            tvjagran->{openCreateAdOneActivity(JAGARAN_GONDHAL)}
            tvbirthday->{openCreateAdOneActivity(BIRTHDAY)}
            tvRetirement->{openCreateAdOneActivity(RETIREMENT)}
            tvSatyaNarayan->{openCreateAdOneActivity(SATYANARAYAN_POOJA)}
            tvMahaPrasad->{openCreateAdOneActivity(MAHAPRASAD)}
            tvOtherEvent->{openCreateAdOneActivity(OTHER_EVENT)}
        }
    }

    fun loadData(){
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
                .getEventMatter()
                .enqueue(object : Callback<ResponseEventMatter> {
                    override fun onResponse(call: Call<ResponseEventMatter>?, response: Response<ResponseEventMatter>?) {
                        if (response!!.code() == 200) {
                            if (response.body()?.status == HttpConstant.SUCCESS) {
                                runOnUiThread {
                                    dismissProgress()
                                    matter = response.body()!!.Matter as ArrayList<Matter>
                                    scrMain.visibility=View.VISIBLE
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

                    override fun onFailure(call: Call<ResponseEventMatter>?, t: Throwable?) {
                        runOnUiThread {
                            dismissProgress()
                            showError(t.toString())
                        }
                    }
                })
    }

    private fun openCreateAdOneActivity(type:Int) {
        startActivity(Intent(applicationContext, CreateEventOneActivity::class.java)
                .putExtra("eventMatter", matter[type-1]))
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
