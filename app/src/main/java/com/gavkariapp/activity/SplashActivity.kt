package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get


class SplashActivity : BaseActivity() {

    // Splash screen timer
    private val SPLASH_TIME_OUT = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set view
        setContentView(R.layout.activity_splash)

        //move to next activity
        moveToNext()

    }

    private fun moveToNext() {

        // Add code to print out the key hash
        /*try {
            val info = packageManager.getPackageInfo(
                    "com.gavkariapp",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }*/

        Handler().postDelayed(
                {
                    //next scrn
                    checkLogin()
                }, SPLASH_TIME_OUT.toLong())
    }

    private fun checkLogin() {
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userId = prefs[ApiConstant.USER_ID, "-1"]

        if (userId == "-1") {

            // This method will be executed once the timer is over
            startActivity(Intent(this.applicationContext, LanguageActivity::class.java))

            // close this activity
            finish()

        } else {

            // This method will be executed once the timer is over
            startActivity(Intent(this.applicationContext, HomeActivity::class.java))

            applyLocale(this)

            // close this activity
            finish()

        }
    }
}

