package com.gavkariapp.constant

import com.gavkariapp.base.MyApplication
import com.gavkariapp.constant.ApiConstant.AVATAR
import com.gavkariapp.constant.ApiConstant.BIO
import com.gavkariapp.constant.ApiConstant.DEVICE_ID
import com.gavkariapp.constant.ApiConstant.MOBILE
import com.gavkariapp.constant.ApiConstant.NAME
import com.gavkariapp.constant.ApiConstant.USER_ID
import com.gavkariapp.constant.ApiConstant.VILLAGE_ID
import com.gavkariapp.constant.ApiConstant.VN_ENGLISH
import com.gavkariapp.constant.ApiConstant.VN_HINDI
import com.gavkariapp.constant.ApiConstant.VN_MARATHI
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get


object AppConstant {

    //event type
    val PUBLIC = 0
    val WEDDING = 1
    val ENGAGEMENT  = 2
    val FIRST_MEMORIAL  = 3
    val HOUSE_WARMING  = 4
    val DASHKRIYA_VIDHI= 5
    val JAGARAN_GONDHAL = 6
    val BIRTHDAY = 7
    val RETIREMENT = 8
    val SATYANARAYAN_POOJA = 9
    val MAHAPRASAD = 10
    val OTHER_EVENT = 11

    //event status
    val CREATED = 0
    val UNDER_REVIEW = 1
    val REJECTED = 2
    val PUBLISHED = 3
    val SOLD = 4

    //plan type
    val NO_PLAN = 0
    val BASIC = 1
    val PREMIUM = 2
    val GOLD = 3

    //publish type
    val PUBLISH_NOW = 1
    val PUBLISH_LATER = 2

    //pay status
    val NO_PAYMENT = 0
    val PAYMENT_DONE = 1


    // News Type
    val GRAM_PANCHAYAT = 1
    val HEALTH_SERVICE = 2
    val SCHOOL = 3
    val SAD_NEWS = 4
    val SARPANCH = 5
    val POLICE_PATIL = 6
    val OTHER_NEWS = 7
    val MP = 8
    val MLA = 9
    val AGRICULTURAL = 10
    val GOVT_SCHEME = 11

    //Buy sale main type
    val ANIMAL = 1
    val MACHINERY = 2
    val EQUIPMENTS = 3

    //All
    var ALL = 0

    //Animal
    var COW = 1
    var BUFFALO = 2
    var HEIFER_COW = 3
    var HEIFER_BUFFALO = 4
    var OX = 5
    var MALE_BUFFALO = 6
    var GOAT = 7
    var MALE_GOAT = 8
    var OTHER_DOMESTIC_ANIMALS = 9

    //machinary
    var TRACTOR = 1
    var PICKUP = 2
    var TEMPO_TRUCK = 3
    var CAR = 4
    var TWO_WHEELER = 5
    var THRESHER = 6
    var KUTTI_MACHINE = 7
    var SPRAY_BLOWER = 8
    var OTHER_MACHINE = 9

    //equipments
    var CULTIVATOR = 1
    var ROTOVATOR = 2
    var PLOUGH = 3
    var TROLLEY = 4
    var SEED_DRILL = 5
    var WATER_MOTOR_PUMP = 6
    var SPRAY_PUMP = 7
    var IRRIGATION_MATERIAL = 8
    var STEEL = 9
    var OTHER = 10


    //user language
    var LANGUAGE = "user_language"
    const val ENGLISH = "en"
    const val MARATHI = "mr"

    //facebook permission
    val EMAIL = "email"

    //social login type
    val FACEBOOK = "facebook"

    //location
    val LATITUDE = ""
    val LONGITUDE = ""

    //home screen
    const val OPEN_SEARCH_VILLAGE_ACTIVITY = 101
    const val OPEN_SEARCH_NEARBY_ACTIVITY = 102
    const val OPEN_MY_AD_DETAIL_ACTIVITY = 103
    const val OPEN_EVENT_VILLAGE_SELECTION_ACTIVITY = 104
    const val OPEN_EVENT_VILLAGE_SELECTION_BY_TAL_ACTIVITY = 105
    const val OPEN_NEARBY_VILLAGE_Directory_ACTIVITY = 106
    const val OPEN_EVENT_FILTER_ACTIVITY = 107
    const val OPEN_NEWS_FILTER_ACTIVITY = 108
    const val REQ_CODE_VERSION_UPDATE = 109

    //create event, select image
    val IMAGE_NOT_SELECTED = 0
    val IMAGE_1_SELECTED = 1
    val IMAGE_2_SELECTED = 2
    val IMAGE_3_SELECTED = 3
    val IMAGE_4_SELECTED = 4

    //notification status
    val ON = 1
    val OFF = 0

    //yes/no
    val YES = 1
    val NO = 0


    //connection added
    val NEW_CONNECTION="new_connection"

    //take userid from preferences
    val prefs = PreferenceHelper.customPrefs(MyApplication.instance, "user_info")
    var userId: String? = prefs[USER_ID, "-1"]
    var villageId: String? = prefs[VILLAGE_ID, "-1"]
    var latitude: String? = prefs[LATITUDE, "19.696430"]
    var longitude: String? = prefs[LONGITUDE, "73.931908"]
    var userName: String? = prefs[NAME, ""]
    var userEmail: String? = prefs[EMAIL, ""]
    var userMobile: String? = prefs[MOBILE, ""]
    var userAvatar: String? = prefs[AVATAR, ""]
    var deviceId: String? = prefs[DEVICE_ID, "-1"]
    var userLang: String? = prefs[LANGUAGE, "-1"]
    var vnEnglish: String? = prefs[VN_ENGLISH, "-1"]
    var vnMarathi: String? = prefs[VN_MARATHI, "-1"]
    var vnHindi: String? = prefs[VN_HINDI, "-1"]
    var bio: String? = prefs[BIO, ""]
    var newConnection: String? = prefs[NEW_CONNECTION, "0"]


}
