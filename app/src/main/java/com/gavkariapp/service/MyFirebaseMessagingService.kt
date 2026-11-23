package com.gavkariapp.service


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.gavkariapp.Model.NotificationBody
import com.gavkariapp.R
import com.gavkariapp.activity.MyNotificationActivity
import com.gavkariapp.activity.NotificationActivity
import com.gavkariapp.constant.ApiConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.set
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject


//https://github.com/firebase/quickstart-android

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        //we will save the token in shared preferences later
        val prefs = PreferenceHelper.customPrefs(this,"device_id")
        prefs[ApiConstant.DEVICE_ID] = token
        Log.e("token",token)
    }

    private val TAG = "MyFirebaseMsgService"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if (remoteMessage!!.data.isNotEmpty()) {
            Log.e(TAG, "Data Payload: " + remoteMessage.data.toString())
            Log.e(TAG, "notification: " + remoteMessage.notification?.body.toString())
            try {
                val params = remoteMessage.data
                val json = JSONObject(params as Map<*, *>)
                Log.e("JSON OBJECT", json.toString())
                sendPushNotification(json)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }

        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //fire base cloud messaging
    private fun sendPushNotification(json: JSONObject) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON $json")
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        try {
            //parsing json data
            val title = json.getString("title")
            val message = json.getString("body")
            val type = json.getString("type")
            val id = json.getString("id")
            //val imageUrl = json.getString("image")

            if (type == "notification"){
                var intent = Intent(this, MyNotificationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                val channelId = "Default"

                val builder = NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_gaav_logo)
                        .setContentIntent(pendingIntent)
                        .setContentTitle(title)
                        .setContentText(message).setAutoCancel(true).setContentIntent(pendingIntent)
                        .setSound(soundUri)
                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
                    manager.createNotificationChannel(channel)
                }
                manager.notify(0, builder.build())
            }else{
                var notificationBody = NotificationBody(type,id)
                var intent = Intent(this, NotificationActivity::class.java)
                intent.putExtra("notificationBody",notificationBody)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
                val channelId = "Default"
                val builder = NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_gaav_logo)
                        .setContentIntent(pendingIntent)
                        .setContentTitle(title)
                        .setContentText(message).setAutoCancel(true).setContentIntent(pendingIntent)
                        .setSound(soundUri)
                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
                    manager.createNotificationChannel(channel)
                }
                manager.notify(0, builder.build())
            }

            /*//creating MyNotificationManager object
            val mNotificationManager = MyNotificationManager(applicationContext)

            //creating an intent for the notification
            val intent = Intent(this, HomeActivity::class.java)

            //if there is no image
            if (imageUrl == "-1") {
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent)
            } else {
                //if there is an image
                //displaying a big notification
                //mNotificationManager.showBigNotification(title, message, imageUrl, intent)
            }*/

        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }

    }

}