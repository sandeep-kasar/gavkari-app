package com.gavkariapp.activity

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.gavkariapp.R
import com.gavkariapp.base.MyApplication
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.AppConstant.LANGUAGE
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.data.PreferenceHelper.set
import com.gavkariapp.helper.LanguageHelper
import com.gavkariapp.interfaces.AlertMessageCallback
import com.gavkariapp.interfaces.BasicBehaviourProvider
import com.gavkariapp.utility.DiaplayDialog
import com.gavkariapp.utility.DisplayMessage
import com.gavkariapp.utility.InternetUtil
import java.util.*


open class BaseActivity : AppCompatActivity(), BasicBehaviourProvider {

    private lateinit var dialog: Dialog

    override fun onResume() {
        super.onResume()
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userLang = prefs[LANGUAGE, "-1"]
        LanguageHelper.setAppLocale(userLang, MyApplication.instance)
    }

    override fun showError(message: String) {
        DisplayMessage.DisplayError(message, this)

    }

    override fun showWarning(message: String) {
        DisplayMessage.DisplayWarning(message, this)

    }

    override fun showSuccess(message: String) {
        DisplayMessage.DisplaySuccess(message, this)
    }

    override fun showCustomMessage(message: String) {

    }

    override fun showProgress() {
        dialog = Dialog(this)
        if (dialog != null) {
            DiaplayDialog.progressDialog(dialog)
        }
    }

    override fun dismissProgress() {
        if (dialog != null) {
            dialog.dismiss()
        }
    }


    override fun showAlert(activity: Activity, alertMessageCallback: AlertMessageCallback,
                           setTitle: String, setMessage: String, setPositiveButton: String,
                           setNegativeButton: String) {
        DiaplayDialog.AlertDialog(activity, alertMessageCallback, setTitle, setMessage,
                setPositiveButton, setNegativeButton)
    }

    override fun waitForInternet() {

        InternetUtil.observe(this, Observer { status ->
            if (status!!) {
                showSuccess("")
//                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                showError(getString(R.string.msg_no_internet))
//                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })
    }

    fun setupToolbar(toolbarId: Int, title: String) {
        val myToolbar = findViewById<View>(toolbarId) as Toolbar
        setSupportActionBar(myToolbar)
        if (supportActionBar != null) {
            supportActionBar?.title = title
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        prefs[LANGUAGE] = languageCode
    }

    fun applyLocale(activity: Activity) {
        val prefs = PreferenceHelper.customPrefs(activity, "user_info")
        var languageCode = prefs[AppConstant.LANGUAGE, "mr"]
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
