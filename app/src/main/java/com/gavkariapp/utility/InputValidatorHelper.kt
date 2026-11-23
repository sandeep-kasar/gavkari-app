package com.gavkariapp.utility

import java.util.regex.Pattern
import android.text.TextUtils


object InputValidatorHelper {

    fun isValidEmail(email: String): Boolean {

        val ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = java.util.regex.Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()

    }

    /*fun isValidTitleWithoutNumber(string: String): Boolean {

        val INPUT_PATTERN = "^[a-zA-Z./,:@&#\u0900-\u097F ]{5,100}$"
        val pattern = Pattern.compile(INPUT_PATTERN)
        val matcher = pattern.matcher(string)
        return matcher.matches()

    }

    fun isValidSubTitleWithoutNumber(string: String): Boolean {

        val INPUT_PATTERN = "^[a-zA-Z./,:@&#\u0900-\u097F ]{20,1000}$"
        val pattern = Pattern.compile(INPUT_PATTERN)
        val matcher = pattern.matcher(string)
        return matcher.matches()

    }*/

//    fun isValidEntryWithNumber(string: String): Boolean {
//
//        val INPUT_PATTERN = "^\\+[0-9]{10,13}\$"
//        val pattern = Pattern.compile(INPUT_PATTERN)
//        val matcher = pattern.matcher(string)
//        if (matcher.matches()) {
//            return false
//        }
//        return true
//    }

    fun isValidPassword(password: String): Boolean {

        //password should be 6 to 12 character long

        return if (password.length < 6) {
            true
        } else password.length > 12


    }

    fun isValidMobile(phone: String): Boolean {

        if (phone.length > 6) {
            return true
        }
        return false
    }

    fun isNullOrEmpty(string: String): Boolean {
        return TextUtils.isEmpty(string)
    }

    fun isMobileNoEnglish(string: String):Boolean{
        val INPUT_PATTERN = "\\d+"
        val pattern = Pattern.compile(INPUT_PATTERN)
        val matcher = pattern.matcher(string)
        return matcher.matches()
    }

}


