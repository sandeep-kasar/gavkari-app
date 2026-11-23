package com.gavkariapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gavkariapp.R

class HelpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_help)
    }
}
