package com.gavkariapp.Model

import java.io.Serializable

data class MyVillageResponse(
        val status: Int,
        val message: String,
        val MyVillageEvent: ArrayList<MyVillageEvent>
) : Serializable

data class MyVillageEvent(
        val id: String,
        val user_id: String,
        val village_id: String,
        val status: String,
        val plan_type: Int,
        val village_boy_id: String,
        val type: Int,
        val created_at: String,
        val event_date: String,
        val event_date_ms: String,
        val latitude: String="0.0",
        val longitude: String="0.0",
        val address: String,
        val location: String,
        val contact_no: String,
        val title: String,
        val family: String,
        val muhurt: String,
        val subtitle: String,
        val subtitle_one: String,
        val subtitle_two: String,
        val subtitle_three: String,
        val subtitle_four: String,
        val subtitle_five: String,
        val note: String,
        val description: String,
        val description_one: String,
        val photo: String,
        val english: String,
        val hindi: String,
        val marathi: String
) : Serializable


data class NewsResponse(
        val status: Int,
        val message: String,
        val News: ArrayList<News>
)

data class MyNewsResponse(
        val status: Int,
        val message: String,
        val News: ArrayList<MyNews>
)

data class News(
        val id: String,
        val user_id : String,
        val village_id:String,
        val assembly_const_id:String,
        val parliament_const_id:String,
        val news_type:Int,
        val status:Int,
        val news_date:String,
        val news_date_ms:Long,
        val title:String,
        val source:String,
        val photo:String,
        val description:String
) : Serializable

data class MyNews(
        val id: String,
        val user_id : String,
        val village_id:String,
        val assembly_const_id:String,
        val parliament_const_id:String,
        val news_type:Int,
        val status:Int,
        val news_date:String,
        val news_date_ms:Long,
        val title:String,
        val source:String,
        val photo:String,
        val description:String,
        val news_media: List<Media>
) : Serializable


data class RemoveConnectionResponse(
        val status: Int,
        val message: String
)


data class DirectoryIntent(
        val village_id : String,
        val is_my_village  : Int
):Serializable

