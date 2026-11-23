package com.gavkariapp.Model

import java.io.Serializable

data class DirectoryListResponse(
        val status: Int,
        val message: String,
        val directoryList: ArrayList<DirectoryList>
)

data class DirectoryList(
        val user_id: String,
        val business: String,
        val b_name: String,
        val b_description: String,
        val mobile: String,
        val avatar: String
):Serializable


data class NotificationListResponse(
        val status: Int,
        val message: String,
        val notificationList: ArrayList<NotificationData>
)

data class NotificationData(
        val title: String,
        val description: String,
        val date: String,
        val photo: String
):Serializable