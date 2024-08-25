package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gavkariapp.Model.ApiResponse
import com.gavkariapp.Model.NotificationData
import com.gavkariapp.Model.NotificationListResponse
import com.gavkariapp.R
import com.gavkariapp.adapter.Notificationadapter
import com.gavkariapp.adapter.onClickRow
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_directory.*
import kotlinx.android.synthetic.main.activity_directory.layNoData
import kotlinx.android.synthetic.main.layout_no_data.*


class MyNotificationActivity : BaseActivity(), onClickRow {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var observer: Observer<ApiResponse<NotificationListResponse, String>>

    private lateinit var notificationListResponse: NotificationListResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_directory)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_notification))
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        initObserver()
        accessDirectory()
    }

    private fun accessDirectory() {

        //login progress
        showProgress()

        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userId: String? = prefs[ApiConstant.USER_ID, "-1"]

        if (InternetUtil.isInternetOn()) {
            //pass data to view model
            homeViewModel.accessMyNotification(userId!!).observe(this, observer)
        } else {
            //observe internet connection
            dismissProgress()
            waitForInternetConnection()
        }


    }

    private fun initObserver() {

        observer = Observer { t ->

            //hide progress bar
            dismissProgress()

            if (t?.response != null) {

                notificationListResponse = t.response!!

                if (notificationListResponse?.status == HttpConstant.SUCCESS) {
                    displayData(t.response!!.notificationList)
                } else {
                    dismissProgress()
                    Log.e("warning", notificationListResponse!!.message)
                    rvDirectory.visibility = View.GONE
                    layNoInternet.visibility = View.GONE
                    layNoDataText.text = getString(R.string.no_notification)
                    layNoData.visibility = View.VISIBLE

                }

            } else {
                Log.e("warning", t?.error)
                dismissProgress()
                showWarning(getString(R.string.msg_unexpected_error))
            }
        }
    }

    private fun displayData(response: ArrayList<NotificationData>) {
        rvDirectory.visibility = View.VISIBLE
        layNoInternet.visibility = View.GONE
        layNoData.visibility = View.GONE
        var notificationadapter = Notificationadapter(response, this)
        rvDirectory.applyVerticalWithDividerLinearLayoutManager()
        rvDirectory.adapter = notificationadapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun waitForInternetConnection() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                rvDirectory.visibility = View.VISIBLE
                layNoInternet.visibility = View.GONE
                layNoData.visibility = View.GONE

            } else {
                layNoInternet.visibility = View.VISIBLE
                rvDirectory.visibility = View.GONE
                layNoData.visibility = View.GONE

            }
        })
    }

    override fun onClickRow(data: NotificationData) {
        startActivity(Intent(this,NotificationDetailActivity::class.java)
                .putExtra("notification_data",data))
    }

}

