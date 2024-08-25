package com.gavkariapp.Model

import java.io.Serializable


data class CreateAdBody(
        val user_id: String,
        val village_id: String,
        val type: Int,
        val title: String,
        val subtitle: String,
        val subtitle_one: String,
        val subtitle_two: String,
        val subtitle_three: String,
        val subtitle_four: String,
        val subtitle_five: String,
        val family: String,
        val description: String,
        val description_one: String,
        val event_date: String,
        val event_date_ms: String,
        val muhurt: String,
        val event_media: List<Media>,
        val address: String,
        val location: String,
        val latitude: String,
        val longitude: String,
        val contact_no: String,
        val note: String,
        val photo: String,
        var event_id: String,
        var event_aid: String,
        var status: Int

) : Serializable

data class Media(
        val id: Int,
        val photo: String
) : Serializable

data class SaleMedia(
        val buysale_id: Int,
        val photo: String
) : Serializable

data class UploadFile(
        val status: String
)

data class MyAdResponse(
        val status: Int,
        val message: String,
        val MyAd: ArrayList<MyEvent>
) : Serializable

data class MyEvent(
        val id: String,
        val event_aid: String,
        val user_id: String,
        val village_id: String,
        val status: Int,
        val type: Int,
        val created_at: String,
        val event_date: String,
        val event_date_ms: String,
        val latitude: String,
        val longitude: String,
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
        val event_media: List<Media>

) : Serializable

data class Design(
        val type: Int,
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
        val address: String,
        val photo: String
) :Serializable

data class CommonResponse(
        val status: Int,
        val message: String
)

data class AdVillageResponse(
        val amount: String,
        val message: String,
        val VillageList: Villages,
        val status: Int
)

data class SaveEventResponse(
        val message: String? = null,
        val event: Event? = null,
        val status: Int? = null
)

data class Event(
        val id : String,
        val user_id: String,
        val village_id: String,
        val status: Int,
        val plan_type:Int,
        val type:Int,
        val event_aid:String,
        val created_at:String,
        val event_date:String,
        val event_date_ms:String,
        val contact_no:String,
        val latitude:String,
        val longitude:String,
        val address:String,
        val location:String,
        val title:String,
        val family:String,
        val muhurt:String,
        val subtitle:String,
        val subtitle_one:String,
        val subtitle_two:String,
        val subtitle_three:String,
        val subtitle_four:String,
        val subtitle_five:String,
        val note:String,
        val photo:String,
        val description:String,
        val description_one:String
)

data class SendSmsBody(
        val user_id: String,
        val event_id: String,
        val message: String,
        val mobile_nums: ArrayList<String>
)

data class BuySaleMediaBody(
        val id: String,
        val user_id: String,
        val fav_user_id:String
)

data class EventMediaResp(
        val status: Int,
        val message: String,
        val photos: ArrayList<Photos>
)

data class BuySaleMedia(
        val status: Int,
        val message: String,
        val photos: ArrayList<Photos>,
        val fav: ArrayList<Fav>,
        val user: UserShort
)

data class Fav(
        val id: String,
        val like_user_id: String,
        val buy_sale_id: String,
        val created_at: String
)

data class Photos(
        val id: String,
        val ad_id: String,
        val photo: String
)

data class CreateNewsBody(
        val id : String,
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
        val news_media: List<Media>,
        val is_edit: Boolean
) : Serializable

data class EventFilterBody(
        var village_id: String,
        val event_type: ArrayList<Int>,
        val event_period: Int
): Serializable

data class NearbyFilterBody(
        var user_id: String,
        val event_type: ArrayList<Int>,
        val event_period: Int
): Serializable

data class NewsFilterBody(
        var village_id: String,
        val news_type: ArrayList<Int>,
        val news_period: Int
): Serializable