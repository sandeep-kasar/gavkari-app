package com.gavkariapp.Model

import java.io.Serializable

data class NotiStatusBody(val user_id : String,val noti_status:Int)

data class NotificationBody(
        var type : String,
        var id:String
):Serializable

data class NotificationResponse(
        val status: Int,
        val message: String,
        val event: ArrayList<Event>,
        val news: ArrayList<News>,
        val salead: ArrayList<BuySale>
) : Serializable


