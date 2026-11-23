package com.gavkariapp.utility

import android.app.Activity
import android.graphics.Color
import android.widget.TextView
import android.widget.Toast
import com.gavkariapp.R


object DisplayMessage {


    fun DisplayError(message: String, activity: Activity) {

        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()

       /* val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        val view = toast.getView()
        view.setBackgroundResource(R.drawable.toast_error)
        val text = view.findViewById(android.R.id.message) as TextView
        text.setTextColor(Color.parseColor("#ffffff"));
        toast.show()*/

    }

    fun DisplayWarning(message: String, activity: Activity) {

        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()

        /*val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        val view = toast.getView()
        view.setBackgroundResource(R.drawable.toast_gray)
        val text = view.findViewById(android.R.id.message) as TextView
        text.setTextColor(Color.parseColor("#ffffff"));
        toast.show()*/

    }

    fun DisplaySuccess(message: String, activity: Activity) {

        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()

        /*val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        val view = toast.getView()
        view.setBackgroundResource(R.drawable.toast_suucess)
        val text = view.findViewById(android.R.id.message) as TextView
        text.setTextColor(Color.parseColor("#ffffff"));
        toast.show()*/

    }

}
