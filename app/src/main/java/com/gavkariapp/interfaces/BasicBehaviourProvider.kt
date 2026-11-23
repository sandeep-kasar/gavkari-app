package com.gavkariapp.interfaces

import android.app.Activity
import com.gavkariapp.interfaces.AlertMessageCallback

interface BasicBehaviourProvider {

    fun showError(message: String)

    fun showWarning(message: String)

    fun showSuccess(message: String)

    fun showCustomMessage(message: String)

    fun showProgress()

    fun dismissProgress()

    fun waitForInternet()

    fun showAlert(activity: Activity, alertMessageCallback: AlertMessageCallback,
                  setTitle: String, setMessage: String, setPositiveButton: String,
                  setNegativeButton: String)


}
