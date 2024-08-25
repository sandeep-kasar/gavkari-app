package com.gavkariapp.utility

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.gavkariapp.R
import com.gavkariapp.interfaces.AlertMessageCallback


object DiaplayDialog {

    fun progressDialog(dialog: Dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_progressbar)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    fun AlertDialog(activity: Activity, alertMessageCallback: AlertMessageCallback,
                    setTitle: String, setMessage: String, setPositiveButton: String,
                    setNegativeButton: String) {

        //Show Information about why you need the permission
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(setTitle)
        builder.setMessage(setMessage)
        builder.setPositiveButton(setPositiveButton) { dialog, which ->
            dialog.cancel()
            alertMessageCallback.setPositiveButton("open setting")
        }
        builder.setNegativeButton(setNegativeButton) { dialog, which -> dialog.cancel() }
        builder.show()
    }
}
