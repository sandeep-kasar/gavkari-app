package com.gavkariapp.utility

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.FileProvider
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.gavkariapp.R
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.app.Activity
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.VectorEnabledTintResources
import androidx.core.os.ConfigurationCompat
import com.bumptech.glide.load.engine.Resource
import com.gavkariapp.BuildConfig
import com.gavkariapp.base.MyApplication
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get


object Util {

    fun getFormatedDate(strDate: String, sourceFormate: String,
                        destinyFormate: String, resources: Resources): String? {
        var df: SimpleDateFormat
        df = SimpleDateFormat(sourceFormate)

        try {
            val currentLocale = ConfigurationCompat.getLocales(resources.configuration)[0]
            val date = df.parse(strDate)
            df = SimpleDateFormat(destinyFormate, currentLocale)
            return df.format(date)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun getFormatedDateEnglish(strDate: String, sourceFormate: String,
                        destinyFormate: String): String? {
        var df: SimpleDateFormat
        df = SimpleDateFormat(sourceFormate)

        try {
            val date = df.parse(strDate)
            df = SimpleDateFormat(destinyFormate, Locale.ENGLISH)
            return df.format(date)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    fun getInMilliSecond(input: String): Long {
        var date: Date? = null
        var millisecondsFromNow: Long = 0
        try {
            date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(input)
            val milliseconds = date!!.time
            millisecondsFromNow = milliseconds - Date().time
            return milliseconds
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return millisecondsFromNow
    }

    //get real path getRealPathFromUri
    fun getRealPathFromUri(context: Context, contentURI: String): String {
        val contentUri = Uri.parse(contentURI)
        val cursor = context.getContentResolver().query(contentUri, null, null, null, null)

        return if (cursor == null) {
            contentUri.path.toString()
        } else {
            cursor!!.moveToFirst()
            val index = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor!!.getString(index)
        }
    }

    //create image file
    fun getOutputMediaFile(directoryName: String): File? {

        // External sdcard location
        val mediaStorageDir = File(
                Environment.getExternalStorageDirectory().getPath(), directoryName)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(directoryName, "Oops! Failed create "
                        + directoryName + " directory")
                return null
            }
        }
        val prefs = PreferenceHelper.customPrefs(MyApplication.instance, "user_info")
        val useId: String? = prefs[ApiConstant.USER_ID, "-1"]
        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.ENGLISH).format(Date())
        val mediaFile: File
        mediaFile = File(mediaStorageDir.path + File.separator
                + "IMG_" +useId+"_"+ timeStamp + ".png")

        return mediaFile
    }

    //create output uri
    fun getOutputMediaFileUri(context: Context, direName: String): Uri {

        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",
                    getOutputMediaFile(direName)!!)

        } else {

            Uri.fromFile(getOutputMediaFile(direName))
        }
    }


    //show alert dialog
    fun showPermissionAlert(context: Context, content: String, title: String) {

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dia_ok_cancel)
        dialog.setCancelable(true)
        dialog.show()

        val btnGrant = dialog.findViewById(R.id.btnGrant) as Button
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val tvTitle = dialog.findViewById(R.id.tvTitle) as TextView
        val tvMessage = dialog.findViewById(R.id.tvMessage) as TextView


        tvTitle.text = title
        tvMessage.text = content

        btnGrant.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()

    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm!!.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    fun getDateNow():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        return currentDate
    }

}