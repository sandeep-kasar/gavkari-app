package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gavkariapp.Model.ApiResponse
import com.gavkariapp.Model.SignInBody
import com.gavkariapp.Model.SignInResponse
import com.gavkariapp.R
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.constant.HttpConstant.SUCCESS
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.helper.UserData
import com.gavkariapp.utility.DeviceInfo.getDeviceManufacturer
import com.gavkariapp.utility.DeviceInfo.getIMEI
import com.gavkariapp.utility.DeviceInfo.getPlatformVersion
import com.gavkariapp.utility.InputValidatorHelper
import com.gavkariapp.utility.InternetUtil
import com.gavkariapp.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : BaseActivity(), View.OnClickListener {

    private lateinit var loginViewmodel: LoginViewModel

    private lateinit var usersignInobserver: Observer<ApiResponse<SignInResponse, String>>

    private lateinit var userData: UserData

    var deviceId: String? = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        loginViewmodel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        userData = UserData(this)
        initSigninObserver()
        btnSignIn.setOnClickListener(this)
        laySignUp.setOnClickListener(this)
        tvForgetPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v) {

            btnSignIn -> signIn()

            laySignUp -> {
                startActivity(Intent(this.applicationContext, SignUpActivity::class.java))
                finish()
            }

            tvForgetPassword ->{
                startActivity(Intent(this.applicationContext, SignUpActivity::class.java))
                finish()
            }
        }
    }

    private fun signIn() {

        //get default prefs
        val prefs = PreferenceHelper.customPrefs(this, "device_id")
        deviceId = prefs[ApiConstant.DEVICE_ID, "-1"]

        //take params
        val mobile = edtMobileSi.text.toString().trim()
        val password = edtPassword.text.toString()
        val device_company = getDeviceManufacturer()
        val imei = getIMEI(this)
        val platform_version = getPlatformVersion()

        //validate the input params
        val isValidInput: Boolean = validate(mobile, password)

        //if input is valid then onClickRow register api
        if (isValidInput && deviceId != null && device_company != null && imei != null && platform_version != null) {

            //create user body
            val userBody = SignInBody(mobile, password, deviceId!!, device_company, imei, platform_version)

            //login progress
            showProgress()

            if (InternetUtil.isInternetOn()) {
                //pass data to view model
                loginViewmodel.signInCall(userBody).observe(this, usersignInobserver)
            } else {
                //observe internet connection
                dismissProgress()
                waitForInternet()
            }
        }

    }

    private fun initSigninObserver() {

        usersignInobserver = Observer { t ->

            //hide progress bar
            dismissProgress()

            if (t?.response != null) {

                var userSignInResponse = t.response

                if (userSignInResponse?.status == SUCCESS) {
                    userData.saveUserSignInInfo(t.response!!,true)
                    setDefaultLang()
                } else {
                    if (userSignInResponse!!.message =="User dose not exist !"){
                            showSuccess(getString(R.string.msg_no_user))
                    }else{
                        showError(userSignInResponse.message)
                    }
                }

            } else {
                showError(t?.error!!)
            }
        }
    }

    private fun validate(mobile: String, password: String): Boolean {


        if (InputValidatorHelper.isNullOrEmpty(mobile)) {

            showError(getString(R.string.warning_empty_mobile))

            return false

        } else if (InputValidatorHelper.isNullOrEmpty(password)) {

            showError(getString(R.string.warning_empty_password))

            return false

        } else if (InputValidatorHelper.isValidPassword(password)) {

            showError(getString(R.string.warning_invalid_password))

            return false

        } else {

            return true
        }


    }

    private fun setDefaultLang() {
        val prefs = PreferenceHelper.customPrefs(this, "user_info")
        var userLang = prefs[AppConstant.LANGUAGE, "-1"]
        if (userLang == "-1") {
            userData.saveLanguage(AppConstant.ENGLISH)
        }

    }

}
