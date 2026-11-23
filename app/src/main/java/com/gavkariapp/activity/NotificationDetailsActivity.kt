package com.gavkariapp.activity

import android.os.Bundle
import com.gavkariapp.Model.NotificationData
import com.gavkariapp.Model.Photos
import com.gavkariapp.R
import com.gavkariapp.adapter.MainSliderAdapter
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.service.GlideImageLoadingService
import com.gavkariapp.utility.Util
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_notification_details.*
import kotlinx.android.synthetic.main.layout_containt_main_notification.*
import ss.com.bannerslider.Slider

class NotificationDetailActivity : BaseActivity(){

    private lateinit var notificationData: NotificationData

    private var Title = ""

    private var status = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_notification_details)
        Slider.init(GlideImageLoadingService(applicationContext))
        setupViews()
    }

    private fun setupViews() {
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_notification))
        initCollapsingToolbar()
        notificationData = intent.getSerializableExtra("notification_data") as NotificationData
        setData()
    }

    private fun initCollapsingToolbar() {

        collapsing_toolbar.title = " "
        appbar.setExpanded(true)
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = getString(R.string.lbl_notification)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun setData() {

        tvtitle.text = notificationData.title.trim()
        tvDescription.text = notificationData.description

        var inputDate = Util.getFormatedDate(notificationData.date,
                "yyyy-MM-dd", "EEEE, d MMMM",resources)

        tvDate.text = inputDate

        var photos = Photos("0","1",notificationData.photo)
        var imageList = ArrayList<Photos>()
        imageList.add(photos)
        slider.setAdapter(MainSliderAdapter(imageList, HttpConstant.BASE_BANNER_DOWNLOAD_URL))

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
