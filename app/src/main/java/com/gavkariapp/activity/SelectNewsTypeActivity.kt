package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gavkariapp.Model.NewsMatter
import com.gavkariapp.Model.ResponseNewsMatter
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant.AGRICULTURAL
import com.gavkariapp.constant.AppConstant.GOVT_SCHEME
import com.gavkariapp.constant.AppConstant.GRAM_PANCHAYAT
import com.gavkariapp.constant.AppConstant.HEALTH_SERVICE
import com.gavkariapp.constant.AppConstant.MLA
import com.gavkariapp.constant.AppConstant.MP
import com.gavkariapp.constant.AppConstant.OTHER_NEWS
import com.gavkariapp.constant.AppConstant.POLICE_PATIL
import com.gavkariapp.constant.AppConstant.SAD_NEWS
import com.gavkariapp.constant.AppConstant.SARPANCH
import com.gavkariapp.constant.AppConstant.SCHOOL
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import kotlinx.android.synthetic.main.activity_news_select.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectNewsTypeActivity : BaseActivity(), View.OnClickListener {

    var matter = ArrayList<NewsMatter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_news_select)
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_create_news))
        tvGramPanchayat.setOnClickListener(this)
        tvHealthService.setOnClickListener(this)
        tvSchool.setOnClickListener(this)
        tvSadNews.setOnClickListener(this)
        tvSarpanch.setOnClickListener(this)
        tvPolicePatil.setOnClickListener(this)
        tvOtherNews.setOnClickListener(this)

        loadData()
    }
    override fun onClick(v: View?) {


        when (v) {
            tvGramPanchayat->{openCreateAdOneActivity(GRAM_PANCHAYAT)}
            tvHealthService->{openCreateAdOneActivity(HEALTH_SERVICE)}
            tvSchool->{openCreateAdOneActivity(SCHOOL)}
            tvSadNews->{openCreateAdOneActivity(SAD_NEWS)}
            tvSarpanch->{openCreateAdOneActivity(SARPANCH)}
            tvPolicePatil->{openCreateAdOneActivity(POLICE_PATIL)}
            tvOtherNews->{openCreateAdOneActivity(OTHER_NEWS)}
        }
    }

    fun loadData(){
        showProgress()
        ApiClient.get().create(ApiInterface::class.java)
                .getNewsMatter()
                .enqueue(object : Callback<ResponseNewsMatter> {
                    override fun onResponse(call: Call<ResponseNewsMatter>?, response: Response<ResponseNewsMatter>?) {
                        if (response!!.code() == 200) {
                            if (response.body()?.status == HttpConstant.SUCCESS) {
                                runOnUiThread {
                                    dismissProgress()
                                    matter = response.body()!!.Matter as ArrayList<NewsMatter>
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

                    override fun onFailure(call: Call<ResponseNewsMatter>?, t: Throwable?) {
                        runOnUiThread {
                            dismissProgress()
                            showError(t.toString())
                        }
                    }
                })
    }

    private fun openCreateAdOneActivity(type:Int) {
        startActivity(Intent(applicationContext, CreateNewsActivity::class.java)
                .putExtra("newsMatter", matter[type-1]))
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
