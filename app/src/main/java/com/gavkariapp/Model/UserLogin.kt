package com.gavkariapp.Model

import java.io.Serializable


data class SignInBody(
        val mobile: String,
        val password: String,
        val device_id: String,
        val device: String,
        val imei: String,
        val platform_version: String
)

data class SignOutBody(
        val user_id: String,
        val device_id: String
)


data class SignInResponse(
        val status: Int,
        val message: String,
        val user: User,
        val village: Village?
)

data class SignOutResponse(
        val status: Int,
        val message: String
)

data class User(
        val id: String,
        val facebook_id: String,
        val name: String,
        val bio: String,
        val email: String,
        val mobile: String,
        val password: String,
        val village_id: String,
        val is_verified: String,
        val is_village_boy: String,
        val noti_status: String,
        val is_active: String,
        val gender: String,
        val device_company: Any,
        val imei: Any,
        val platform_version: Any,
        val created_at: String,
        val avatar: Any,
        val device_id: String
)

data class UserShort(
        val name: String,
        val mobile: String
)

data class FacebookSignInBody(
        val facebook_id: String,
        val name: String,
        val email: String,
        val gender: String,
        var device_id: String,
        val device_company: String,
        val imei: String,
        val platform_version: String,
        var village_id: String

): Serializable

data class SignUpInput(
        val name: String,
        val mobile: String,
        val password: String
) : Serializable

data class SignUpBody(
        val name: String,
        val mobile: String,
        val password: String,
        val village_id: String,
        val device_id: String,
        val device_company: String,
        val imei: String,
        val platform_version: String
)


data class DistrictBody(
        val state_id: String
)

data class TalukaBody(
        val state_id: String,
        val district_id: String
)

data class VillageBody(
        val state_id: String,
        val district_id: String,
        val taluka_id: String,
        val village_by: String
) : Serializable


data class StateResponse(
        val status: Int,
        val message: String,
        val state: ArrayList<State>
)

data class State(
        val id: String,
        val english: String,
        val hindi: String,
        val marathi: String
)


data class DistrictResponse(
        val status: Int,
        val message: String,
        val districtList: ArrayList<District>
)

data class District(
        val id: String,
        val state_id: String,
        val english: String,
        val hindi: String,
        val marathi: String
)


data class TalukaResponse(
        val status: Int,
        val message: String,
        val talukaList: ArrayList<Taluka>
)

data class Taluka(
        val id: String,
        val state_id: String,
        val district_id: String,
        val english: String,
        val hindi: String,
        val marathi: String
)

data class VillageResponse(
        val status: Int,
        val message: String,
        val villageList: ArrayList<Village>
)

data class Village(
        val id: String,
        val state_id: String,
        val district_id: String,
        val taluka_id: String,
        val english: String,
        val hindi: String,
        val marathi: String,
        val latitude: String,
        val longitude: String
) :Serializable


data class RequestOtp(
        val mobile: String,
        val user_id: String
)

data class OtpResponse(
        val status: Int,
        val message: String
)

data class VerifyMobile(
        val user_id: String,
        val otp: String
)

data class ProfileUpdateBody(
        val id: String,
        val name: String,
        val email: String,
        val bio: String,
        val mobile: String,
        val village_id: String,
        val avatar: String?

)

data class CreateDirectoryBody(
        val user_id: String,
        val village_id: String,
        val village_boy_id: String,
        val business: String,
        val b_name: String,
        val b_description: String,
        val mobile: String,
        val avatar: String?,
        var api_call : Int

)

data class MyDirectoryResponse(
        val status: Int,
        val message: String,
        val directory: MyDirectory
)

data class MyDirectory(
        val id: String,
        val user_id: String,
        val village_id: String,
        val village_boy_id: String,
        val business: String,
        val b_name: String,
        val b_description: String,
        val mobile: String,
        val avatar: String?
)

data class AccountInfoResponse(
        val status: Int,
        val message: String,
        val account: AccountInfo
)

data class AccountInfo(
        val id: String,
        val user_id: String,
        val update_status: String,
        val acct_holder_name: String,
        val account_no: String,
        val ifsc_code: String,
        val branch_name: String,
        val updated_at: String?
)

data class AccountInfoBody(
        val user_id: String,
        val acct_holder_name: String,
        val account_no: String,
        val ifsc_code: String,
        val branch_name: String
)

data class RefundHistoryResponse(
        val status: Int,
        val message: String,
        val refund: List<RefundHistory>
)

data class RefundHistory(
        val title:String,
        val subtitle:String,
        val id:String,
        val user_id:String,
        val village_boy_id:String,
        val event_id:String,
        val amount:String,
        val transaction_no:String,
        val refund_date:String
)