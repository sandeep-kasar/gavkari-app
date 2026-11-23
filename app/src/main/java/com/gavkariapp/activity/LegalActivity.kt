package com.gavkariapp.activity

import android.os.Bundle

import kotlinx.android.synthetic.main.activity_legal.*
import com.gavkariapp.BuildConfig
import com.gavkariapp.R


class LegalActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_legal)
        setupToolbar(R.id.toolbarHome, getString(R.string.lbl_legal))
        webLegal.clearCache(true)
        webLegal.loadUrl(BuildConfig.MEDIA+"terms/index.html")

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
