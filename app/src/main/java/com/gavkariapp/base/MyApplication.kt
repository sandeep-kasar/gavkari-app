package com.gavkariapp.base

import android.app.Application
import android.os.StrictMode
import com.gavkariapp.utility.InternetUtil
import com.google.firebase.FirebaseApp


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        InternetUtil.init(this)
        FirebaseApp.initializeApp(this)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        instance = this
    }

    companion object {
        lateinit var instance: MyApplication
            private set
    }
}