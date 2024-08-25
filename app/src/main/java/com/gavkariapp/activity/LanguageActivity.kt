package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.data.PreferenceHelper
import kotlinx.android.synthetic.main.activity_language.*


class LanguageActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        setDefaultLanguage()
        btnNext.setOnClickListener(this)
        radioEnglish.setOnClickListener(this)
        radioMarathi.setOnClickListener(this)
        setLocale(this,"mr")
    }

    override fun onClick(v: View?) {

        when (v) {

            radioEnglish -> {
                  setLocale(this,"en")
            }

            radioMarathi -> {
                  setLocale(this,"mr")
            }

            btnNext -> {
                startActivity(Intent(applicationContext, SignUpActivity::class.java))
                finish()
            }

        }

    }

    private fun setDefaultLanguage() {
        radioMarathi.isChecked = true
    }
}
