package com.gavkariapp.activity

import android.os.Bundle
import android.view.View
import com.gavkariapp.Model.EventMediaResp
import com.gavkariapp.Model.Photos
import com.gavkariapp.R
import com.gavkariapp.adapter.MainSliderAdapter
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.constant.HttpConstant.BASE_EVENT_DOWNLOAD_URL
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.com.bannerslider.Slider
import ss.com.bannerslider.indicators.IndicatorShape




class MediaActivity : BaseActivity() {

    private lateinit var slider: Slider
    private var imageList = ArrayList<Photos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_media)
        setupViews()
    }

    private fun setupViews() {

        //init view
        slider = findViewById<View>(R.id.banner_slider) as Slider

        val intent = intent

        if (intent.hasExtra("eventId")) {
            var eventId = intent.getStringExtra("eventId")
            getEventMedia(eventId)
        }

        if (intent.hasExtra("newsId")) {
            var newsId = intent.getStringExtra("newsId")
            getNewsMedia(newsId)
        }

    }


    private fun getEventMedia(eventId:String) {

        showProgress()

        eventId?.let {
            ApiClient.get().create(ApiInterface::class.java)
                    .getEventMedia(it)
                    .enqueue(object : Callback<EventMediaResp> {
                        override fun onResponse(call: Call<EventMediaResp>?, response: Response<EventMediaResp>?) {
                            if (response!!.code() == 200) {
                                var response = response!!.body()
                                if (response?.status == HttpConstant.SUCCESS) {
                                    dismissProgress()
                                    imageList= response.photos
                                    //set image
                                    slider.postDelayed({
                                        slider.setAdapter(MainSliderAdapter(imageList,BASE_EVENT_DOWNLOAD_URL))
                                        slider.setSelectedSlide(0)
                                    }, 150)

                                    //set indicator
                                    slider.setIndicatorStyle(IndicatorShape.CIRCLE)
                                    slider.setInterval(3000)
                                    slider.isFocusableInTouchMode

                                } else {
                                    runOnUiThread {
                                        dismissProgress()

                                    }
                                }

                            }
                        }

                        override fun onFailure(call: Call<EventMediaResp>?, t: Throwable?) {
                            runOnUiThread {
                                showError(getString(R.string.warning_try_later))
                                dismissProgress()
                            }
                        }
                    })
        }
    }

    private fun getNewsMedia(eventId:String) {

        showProgress()

        eventId?.let {
            ApiClient.get().create(ApiInterface::class.java)
                    .getEventMedia(it)
                    .enqueue(object : Callback<EventMediaResp> {
                        override fun onResponse(call: Call<EventMediaResp>?, response: Response<EventMediaResp>?) {
                            if (response!!.code() == 200) {
                                var response = response!!.body()
                                if (response?.status == HttpConstant.SUCCESS) {
                                    dismissProgress()
                                    imageList= response.photos
                                    //set image
                                    slider.postDelayed({
                                        slider.setAdapter(MainSliderAdapter(imageList,BASE_EVENT_DOWNLOAD_URL))
                                        slider.setSelectedSlide(0)
                                    }, 150)

                                    //set indicator
                                    slider.setIndicatorStyle(IndicatorShape.CIRCLE)

                                } else {
                                    runOnUiThread {
                                        dismissProgress()

                                    }
                                }

                            }
                        }

                        override fun onFailure(call: Call<EventMediaResp>?, t: Throwable?) {
                            runOnUiThread {
                                showError(getString(R.string.warning_try_later))
                                dismissProgress()
                            }
                        }
                    })
        }
    }
}
