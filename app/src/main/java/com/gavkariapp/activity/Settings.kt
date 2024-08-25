package com.gavkariapp.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.gavkariapp.Model.CommonResponse
import com.gavkariapp.Model.NotiStatusBody
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.OFF
import com.gavkariapp.constant.AppConstant.ON
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.data.PreferenceHelper.set
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import kotlinx.android.synthetic.main.layout_setting_change_lang.*
import kotlinx.android.synthetic.main.layout_setting_notification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class Settings : BaseActivity(), View.OnClickListener {

    var NOTIFICATION_STATUS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyLocale(this)
        setContentView(R.layout.activity_settings)
        setupToolbar(R.id.toolbarHome, getString(R.string.title_setting))
        setUpView()
        btnChangeLang.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        when (v) {

            btnChangeLang -> {
                selectLanguage()
            }

        }
    }

    private fun setUpView() {

        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userLang = prefs[AppConstant.LANGUAGE, "-1"]
        var notiStatus = prefs[ApiConstant.NOTI_STATUS, "0"]

        when (userLang) {
            "mr" -> tvAppLang.text = getString(R.string.lbl_app_lang) + " " + "मराठी"
            "hi" -> tvAppLang.text = getString(R.string.lbl_app_lang) + " " + "हिंदी"
            else -> tvAppLang.text = getString(R.string.lbl_app_lang) + " " + "English"
        }

        if (notiStatus=="1"){
            switchNotification.isChecked = true
        }

        if (notiStatus=="0"){
            switchNotification.isChecked = false
        }


        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                NOTIFICATION_STATUS = ON
                updateStatus()
            } else {
                NOTIFICATION_STATUS = OFF
                updateStatus()
            }

        }
    }

    private fun updateStatus() {
        showProgress()
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userId = prefs[ApiConstant.USER_ID, "-1"]
        ApiClient.get().create(ApiInterface::class.java)
                .updateNotificationStatus(NotiStatusBody(userId!!, NOTIFICATION_STATUS))
                .enqueue(object : Callback<CommonResponse> {
                    override fun onResponse(call: Call<CommonResponse>?, response: Response<CommonResponse>?) {
                        if (response!!.code() == 200) {
                            runOnUiThread {
                                if (response.body()?.status == HttpConstant.SUCCESS) {

                                    val prefs = PreferenceHelper.customPrefs(this@Settings, "user_info")

                                    dismissProgress()

                                    if (NOTIFICATION_STATUS == ON) {
                                        switchNotification.text = getString(R.string.on)
                                        prefs[ApiConstant.NOTI_STATUS] = "1"

                                    }

                                    if (NOTIFICATION_STATUS == OFF) {
                                        switchNotification.text = getString(R.string.off)
                                        prefs[ApiConstant.NOTI_STATUS] = "0"
                                    }
                                }

                                if (response.body()?.status == HttpConstant.DUPLICATE_DATA) {
                                    runOnUiThread {
                                        dismissProgress()
                                        showWarning(response.body()!!.message)
                                    }
                                }
                            }

                        } else {
                            if (response.errorBody() != null) {
                                runOnUiThread {
                                    dismissProgress()
                                    showError(response.errorBody().toString())
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>?, t: Throwable?) {
                        dismissProgress()
                        showError(t.toString())
                    }
                })
    }

    private fun selectLanguage() {
        val dialog = Dialog(this@Settings)
        dialog.setContentView(R.layout.layout_select_language)
        dialog.setTitle(getString(R.string.lbl_select_your_language))
        dialog.show()
        val radioEnglish = dialog.findViewById(R.id.radioEnglish) as Button
        val radioMarathi = dialog.findViewById(R.id.radioMarathi) as Button
        radioEnglish.setOnClickListener {
            setLocaleLanguage("en")
            dialog.dismiss()
        }
        radioMarathi.setOnClickListener {
            setLocaleLanguage("mr")
            dialog.dismiss()
        }
    }

    fun setLocaleLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = this.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        prefs[AppConstant.LANGUAGE] = languageCode
        val i = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
        ActivityCompat.finishAffinity(this@Settings)
    }

}

