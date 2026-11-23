package com.gavkariapp.helper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.gavkariapp.Model.SignInResponse
import com.gavkariapp.activity.HomeActivity
import com.gavkariapp.constant.ApiConstant.ACCT_UPDATE_STATUS
import com.gavkariapp.constant.ApiConstant.AVATAR
import com.gavkariapp.constant.ApiConstant.BIO
import com.gavkariapp.constant.ApiConstant.CREATED_AT
import com.gavkariapp.constant.ApiConstant.DEVICE_COMPANY
import com.gavkariapp.constant.ApiConstant.DEVICE_ID
import com.gavkariapp.constant.ApiConstant.DISTRICT_ID
import com.gavkariapp.constant.ApiConstant.EMAIL
import com.gavkariapp.constant.ApiConstant.FACEBOOK_ID
import com.gavkariapp.constant.ApiConstant.GENDER
import com.gavkariapp.constant.ApiConstant.IMEI
import com.gavkariapp.constant.ApiConstant.IS_ACTIVE
import com.gavkariapp.constant.ApiConstant.IS_VERIFIED
import com.gavkariapp.constant.ApiConstant.IS_VILLAGE_BOY
import com.gavkariapp.constant.ApiConstant.LATITUDE
import com.gavkariapp.constant.ApiConstant.LONGITUDE
import com.gavkariapp.constant.ApiConstant.MOBILE
import com.gavkariapp.constant.ApiConstant.NAME
import com.gavkariapp.constant.ApiConstant.NOTI_STATUS
import com.gavkariapp.constant.ApiConstant.PLATORM_VERSION
import com.gavkariapp.constant.ApiConstant.STATE_ID
import com.gavkariapp.constant.ApiConstant.TALUKA_ID
import com.gavkariapp.constant.ApiConstant.USER_ID
import com.gavkariapp.constant.ApiConstant.VILLAGE_ID
import com.gavkariapp.constant.ApiConstant.VN_ENGLISH
import com.gavkariapp.constant.ApiConstant.VN_HINDI
import com.gavkariapp.constant.ApiConstant.VN_MARATHI
import com.gavkariapp.constant.AppConstant.LANGUAGE
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.set

class UserData(internal var appCompatActivity: AppCompatActivity) {

    //get custom prefs
    val prefs = PreferenceHelper.customPrefs(appCompatActivity, "user_info")

    fun saveUserSignInInfo(response: SignInResponse, isStart :Boolean) {

        //set any type of value in prefs
        prefs[USER_ID] = response.user.id
        prefs[FACEBOOK_ID] = response.user.facebook_id
        prefs[NAME] = response.user.name
        prefs[BIO] = response.user.bio
        prefs[EMAIL] = response.user.email
        prefs[MOBILE] = response.user.mobile
        prefs[VILLAGE_ID] = response.user.village_id
        prefs[IS_VERIFIED] = response.user.is_verified
        prefs[IS_VILLAGE_BOY] = response.user.is_village_boy
        prefs[NOTI_STATUS] = response.user.noti_status
        prefs[IS_ACTIVE] = response.user.is_active
        prefs[GENDER] = response.user.gender
        prefs[DEVICE_COMPANY] = response.user.device_company
        prefs[IMEI] = response.user.imei
        prefs[PLATORM_VERSION] = response.user.platform_version
        prefs[CREATED_AT] = response.user.created_at
        prefs[AVATAR] = response.user.avatar
        prefs[DEVICE_ID] = response.user.device_id

        prefs[STATE_ID] = response.village?.state_id
        prefs[DISTRICT_ID] = response.village?.district_id
        prefs[TALUKA_ID] = response.village?.taluka_id
        prefs[VN_ENGLISH] = response.village?.english
        prefs[VN_HINDI] = response.village?.hindi
        prefs[VN_MARATHI] = response.village?.marathi
        prefs[LATITUDE] = response.village?.latitude
        prefs[LONGITUDE] = response.village?.longitude


        //switch home activity
        if (isStart){
            startNewActivity()
        }
    }

    fun saveAccountStatus(status: Int) {

        //set any type of value in prefs
        prefs[ACCT_UPDATE_STATUS] = status
    }

    /**
     * start MainActivity
     */
    fun startNewActivity() {
        appCompatActivity.startActivity(Intent(appCompatActivity, HomeActivity::class.java))
        appCompatActivity.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityCompat.finishAffinity(appCompatActivity)
    }

    fun saveLanguage(lang: String) {
        prefs[LANGUAGE] = lang
    }


}

