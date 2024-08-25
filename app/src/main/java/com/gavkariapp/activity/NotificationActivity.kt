package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gavkariapp.Model.*
import com.gavkariapp.R
import com.gavkariapp.adapter.NotificationAdapter
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.utility.applyVerticalWithDividerLinearLayoutManager
import com.gavkariapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_directory.*
import kotlinx.android.synthetic.main.activity_directory.layNoData
import kotlinx.android.synthetic.main.layout_no_data.*
import java.util.*
import kotlin.collections.ArrayList


class NotificationActivity : BaseActivity(),NotificationAdapter.OnItemClickListener {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var observer: Observer<ApiResponse<NotificationResponse, String>>

    private lateinit var notificationResponse: NotificationResponse

    lateinit var notificationList: LinkedList<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_directory)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_notification))
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        initObserver()
        accessNotification()
    }

    override fun onItemClick(obj: Any) {
        when (obj) {

            is Event -> {

                var media = ArrayList<Media>()
                var myEvent = MyEvent(obj.id,obj.event_aid,obj.user_id,obj.village_id,obj.status,obj.type,obj.created_at,obj.event_date,
                        obj.event_date_ms,obj.latitude,obj.longitude,obj.address,obj.location,obj.contact_no,obj.title,obj.family,obj.muhurt,
                        obj.subtitle,obj.subtitle_one,obj.subtitle_two,obj.subtitle_three,obj.subtitle_four,obj.subtitle_five,obj.note,
                        obj.description,obj.description_one,obj.photo,media)

                startActivity(Intent(this, EventDetailActivity::class.java).putExtra("VillageResponse", myEvent))

            }
            is News -> {
                startActivity(Intent(this, NewsDetailActivity::class.java).putExtra("news_data", obj))
            }
            is BuySale -> {
                startActivity(Intent(applicationContext,BuySaleDetailActivity::class.java).putExtra("buySaleData",obj))
            }
            else -> {

            }
        }
    }

    private fun accessNotification() {

        showProgress()

        var intent = intent.getSerializableExtra("notificationBody") as NotificationBody
        //Toast.makeText(this, intent.type, Toast.LENGTH_LONG).show()

        var notificationBody = NotificationBody(intent.type,intent.id)

        if (InternetUtil.isInternetOn()) {
            //pass data to view model
            homeViewModel.accessNotification(notificationBody).observe(this, observer)
        } else {
            //observe internet connection
            dismissProgress()
            waitForInternetConnection()
        }


    }

    private fun initObserver() {

        observer = Observer { t ->

            dismissProgress()

            if (t?.response != null) {

                notificationResponse = t.response!!

                if (notificationResponse?.status == HttpConstant.SUCCESS) {
                    displayData(t.response!!)
                } else {
                    dismissProgress()
                    Log.e("warning", notificationResponse!!.message)
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

    private fun displayData(response: NotificationResponse) {

        rvDirectory.visibility = View.VISIBLE
        layNoInternet.visibility = View.GONE
        layNoData.visibility = View.GONE

        notificationList = LinkedList<Any>()
        for (entry in response!!.event) {
            notificationList.add(entry)
        }
        for (entry in response!!.news) {
            notificationList.add(entry)
        }
        for (entry in response!!.salead) {
            notificationList.add(entry)
        }

        var notificationadapter = NotificationAdapter(this,notificationList, this)
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
}

