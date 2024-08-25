@file:Suppress("DEPRECATION")

package com.gavkariapp.utility

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.webkit.PermissionRequest
import com.gavkariapp.activity.BaseActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*


object DeviceInfo {

    private var imei: String? = null
    private var platform_version: String? = null
    private var device: String? = null


    /**
     * get device imei
     */
    fun getIMEI(activity: BaseActivity): String? {

        try {

            val tm = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            if (TextUtils.isEmpty(imei)) {

                Dexter.withActivity(activity)
                        .withPermission(Manifest.permission.READ_PHONE_STATE)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                imei = tm.deviceId
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                getDummyIMEI()
                            }

                            override fun onPermissionRationaleShouldBeShown(permission: com.karumi.dexter.listener.PermissionRequest?, token: PermissionToken?) {
                                getDummyIMEI()
                            }


                        }).check()

            }

        } catch (e: Exception) {
            println(e)
        } finally {
            getDummyIMEI()
        }

        return imei
    }

    /**
     * get Platform Version
     */
    fun getPlatformVersion(): String? {

        platform_version = Build.VERSION.RELEASE

        return platform_version
    }

    /**
     * get Device Manufacturer
     */

    fun getDeviceManufacturer(): String? {

        device = Build.MANUFACTURER

        return device

    }

    /**
     * duplicate imei
     */
    fun getDummyIMEI() {

        val d = System.currentTimeMillis()
        val r = Random()
        val i1 = r.nextInt(99 - 10) + 10
        imei = i1.toString() + d.toString()
        Random().nextInt(10)
    }


}
