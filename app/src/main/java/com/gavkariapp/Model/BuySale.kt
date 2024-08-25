package com.gavkariapp.Model

import java.io.Serializable

data class BuySaleBody(
        val user_id:String,
        val village_id:String,
        val latitude:String,
        val longitude:String
)

data class BuySaleFavBody(
        val user_id:String,
        val tab_type:Int,
        val latitude:String,
        val longitude:String
)

data class BuySaleTypeBody(
        val user_id:String,
        val type:Int,
        val sort:Int,
        val filter:Int,
        val latitude:String,
        val longitude:String
)

data class BuySaleResponse(
        val status: Int,
        val message: String,
        val BuySaleAds: ArrayList<BuySale>
)

data class BuySale (
        var id:String,
        var user_id:String,
        var village_id:String,
        var status:Int,
        var tab_type:Int,
        var type:Int,
        var name:String,
        var price:String,
        var breed:String,
        var pregnancies_count:Int,
        var pregnancy_status:Int,
        var milk:Int,
        var weight:String,
        var company:String,
        var model:String,
        var year:String,
        var km_driven:String,
        var power:String,
        var capacity:String,
        var material:String,
        var tynes_count:String,
        var size:String,
        var phase:String,
        var latitude:String,
        var longitude:String,
        var village_en:String,
        var village_mr:String,
        var created_at:String,
        var photo:String,
        var title:String,
        var description:String,
        var fav_user_id:String,
        var distance:Double,
        var fromActivity:String
):Serializable

data class SaleCreateBody (
        var id: String,
        var user_id:String,
        var village_id:String,
        var status:Int,
        var tab_type:Int,
        var type:Int,
        var name:String,
        var price:String,
        var breed:String,
        var pregnancies_count:String,
        var pregnancy_status:String,
        var milk:String,
        var weight:String,
        var company:String,
        var model:String,
        var year:String,
        var km_driven:String,
        var power:String,
        var capacity:String,
        var material:String,
        var tynes_count:String,
        var size:String,
        var phase:String,
        var latitude:String,
        var longitude:String,
        var village_en:String,
        var village_mr:String,
        var miliseconds:String,
        var photo:String,
        var title:String,
        var description:String,
        var sale_media: List<Media>

):Serializable

data class MySaleAdResponse(
        val status: Int,
        val message: String,
        val MySaleAds: ArrayList<MySaleAd>
)

data class MySaleAd (
        var id:String,
        var user_id:String,
        var village_id:String,
        var status:Int,
        var tab_type:Int,
        var type:Int,
        var name:String,
        var price:String,
        var breed:String,
        var pregnancies_count:Int,
        var pregnancy_status:Int,
        var milk:Int,
        var weight:String,
        var company:String,
        var model:String,
        var year:String,
        var km_driven:String,
        var power:String,
        var capacity:String,
        var material:String,
        var tynes_count:String,
        var size:String,
        var phase:String,
        var latitude:String,
        var longitude:String,
        var village_en:String,
        var village_mr:String,
        var miliseconds:String,
        var created_at:String,
        var photo:String,
        var title:String,
        var description:String,
        var fav_user_id:String,
        var buysale_media: List<Media>

):Serializable

data class AddFavBody(
        val id:String,
        val user_id:String,
        val type:Int
)

data class BuySaleType(
        val tab_type:Int,
        var type:Int,
        var name: String
):Serializable

