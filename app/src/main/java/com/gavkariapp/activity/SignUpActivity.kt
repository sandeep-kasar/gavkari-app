package com.gavkariapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gavkariapp.Model.SignUpInput
import com.gavkariapp.R
import com.gavkariapp.utility.InputValidatorHelper
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btnContinue.setOnClickListener(this)
        laySignIn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v) {

            btnContinue -> signUp()

            laySignIn -> {
                startActivity(Intent(applicationContext, SignInActivity::class.java))
                finish()
            }
        }
    }

    private fun signUp() {

        var name = edtName.text.toString()
        var mobile = edtMobile.text.toString()
        var password = edtPassword.text.toString()

        //validate the input params
        val isValidInput: Boolean = validate(name, mobile, password)

        //if input is valid then onClickRow register api
        if (isValidInput) {
            var signUpInput = SignUpInput(name, mobile, password)
            startActivity(Intent(applicationContext, LocationActivity::class.java)
                    .putExtra("signUpInput", signUpInput))
        }
    }

    private fun validate(name: String, mobile: String, password: String): Boolean {


        if (InputValidatorHelper.isNullOrEmpty(name)) {

            showError(getString(R.string.warning_empty_name))

            return false

        } else if (InputValidatorHelper.isNullOrEmpty(mobile)) {

            showError(getString(R.string.warning_empty_mobile))

            return false

        } else if (!InputValidatorHelper.isValidMobile(mobile)) {

            showError(getString(R.string.warning_invalid_mobile))

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
}
