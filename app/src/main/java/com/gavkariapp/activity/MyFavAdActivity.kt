package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.BuySaleAdapter
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_my_fav_ad.*
import kotlinx.android.synthetic.main.layout_no_data.*


class MyFavAdActivity : BaseActivity() ,BuySaleAdapter.OnItemClickListener{

    lateinit var buySaleAdapter: BuySaleAdapter

    lateinit var homeViewModel: HomeViewModel

    var tab_type = 0

    lateinit var observer: Observer<ApiResponse<BuySaleResponse, String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_my_fav_ad)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_favourite))
        setupView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onItemClick(buysale: BuySale) {
        buysale.fromActivity = "MyFavAdActivity"
        startActivity(Intent(applicationContext,BuySaleDetailActivity::class.java).putExtra("buySaleData",buysale))
    }


    fun setupView(){
        homeViewModel = HomeViewModel()
        tab_type = intent.getIntExtra("tab_type",0)
        rvBuy.applyVerticalWithDividerLinearLayoutManager()
        initObserver()
        accessData()

    }

    fun initObserver(){
        observer = Observer { t ->

            if (t?.response != null) {
                var buySaleResponse = t.response
                if (buySaleResponse?.status == HttpConstant.SUCCESS) {
                    displayData(buySaleResponse)
                }else if (buySaleResponse?.status == HttpConstant.NO_DATA_AVAILABLE){
                    nvBuy.visibility = View.GONE
                    layNoInternetBuy.visibility = View.GONE
                    proBuy.visibility= View.GONE
                    layNoDataBuy.visibility = View.VISIBLE
                    layNoDataText.text = getString(R.string.lbl_not_like_ad)
                }

            } else {
                Log.e("warning", t?.error!!)
                showError(getString(R.string.msg_unexpected_error))
            }
        }
    }

    fun accessData(){

        if (InternetUtil.isInternetOn()) {
            val prefs = PreferenceHelper.customPrefs(applicationContext, "user_info")
            var user_id : String? = prefs[ApiConstant.USER_ID, ""]
            var latitude : String? = prefs[ApiConstant.LATITUDE, ""]
            var longitude : String? = prefs[ApiConstant.LONGITUDE, ""]
            var buySaleBody = BuySaleFavBody(user_id!!,tab_type!!,latitude!!,longitude!!)
            homeViewModel.getbuySaleFavData(buySaleBody).observe(this, observer)
        } else {
            waitForInternetCon()
        }
    }

    fun displayData(buySale: BuySaleResponse){

        proBuy.visibility= View.GONE
        nvBuy.visibility = View.VISIBLE
        layNoInternetBuy.visibility = View.GONE
        layNoDataBuy.visibility = View.GONE

        buySaleAdapter = BuySaleAdapter(buySale.BuySaleAds, this)
        rvBuy.adapter = buySaleAdapter

    }

    fun waitForInternetCon() {
        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                accessData()
            } else {
                layNoInternetBuy.visibility = View.VISIBLE
                rvBuy.visibility = View.GONE
                layNoDataBuy.visibility = View.GONE
                proBuy.visibility=View.GONE
            }
        })
    }
}
