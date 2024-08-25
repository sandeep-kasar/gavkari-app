package com.gavkariapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.gavkariapp.R
import kotlinx.android.synthetic.main.activity_success.*



class SuccessActivity : BaseActivity() {

    lateinit var screenType : String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_success)

        screenType = intent.getStringExtra("screen_type")

        if (screenType=="news"){
            tvMessage.text = getString(R.string.lbl_news_success_msg)
            btnCheckAd.text = getString(R.string.lbl_check_news)
        }

        if (screenType=="saleAd"){
            tvMessage.text = getString(R.string.lbl_ad_success_msg)
            btnCheckAd.text = getString(R.string.lbl_check_ad)
        }

        btnCheckAd.setOnClickListener {
            if (screenType=="event"){
                openMyAd()
            }else if(screenType=="news"){
                openMyNews()
            }else{
                openMyAds()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (screenType=="event"){
            openMyAd()
        }else if(screenType=="news"){
            openMyNews()
        }else{
            openMyAds()
        }
    }

    fun openMyAd(){
        startActivity(Intent(applicationContext, MyEventActivity::class.java))
        ActivityCompat.finishAffinity(this@SuccessActivity)
    }

    fun openMyNews(){
        startActivity(Intent(applicationContext, MyNewsActivity::class.java))
        ActivityCompat.finishAffinity(this@SuccessActivity)
    }

    fun openMyAds(){
        startActivity(Intent(applicationContext, MySaleAdActivity::class.java))
        finish()
    }
}
