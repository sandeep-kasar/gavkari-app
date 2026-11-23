package com.gavkariapp.network

import com.gavkariapp.Model.*
import com.gavkariapp.constant.HttpConstant.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST


interface ApiInterface {

    @POST(SIGN_IN)
    @Headers(JSON_TYPE)
    fun signIn(@Body userSignInBody: SignInBody): Call<SignInResponse>

    @POST(SIGN_OUT)
    @Headers(JSON_TYPE)
    fun signOut(@Body signOutBody: SignOutBody): Call<SignOutResponse>

    @POST(SIGN_UP)
    @Headers(JSON_TYPE)
    fun signUp(@Body signUpBody: SignUpBody): Call<SignInResponse>

    @GET(GET_STATE)
    fun getState(): Call<StateResponse>

    @POST(GET_DISTRICT)
    @Headers(JSON_TYPE)
    fun getDistrict(@Body districtBody: DistrictBody): Call<DistrictResponse>

    @POST(GET_TALUKA)
    @Headers(JSON_TYPE)
    fun getTaluka(@Body talukaBody: TalukaBody): Call<TalukaResponse>

    @POST(GET_VILLAGE)
    @Headers(JSON_TYPE)
    fun getVillage(@Body villageBody: VillageBody): Call<VillageResponse>

    @POST("$MY_VILLAGE")
    @Headers(CACHE_CONTROL)
    fun aceesMyVillageData(@Body eventFilterBody: EventFilterBody): Call<MyVillageResponse>

    @POST("$NEWS_VILLAGES")
    @Headers(CACHE_CONTROL)
    fun accessNews(@Body newsFilterBody: NewsFilterBody): Call<NewsResponse>

    @POST("$GET_BUY_SALE")
    @Headers(CACHE_CONTROL)
    fun accessBuySale(@Body buySaleBody: BuySaleBody): Call<BuySaleResponse>

    @POST("$CREATE_AD")
    @Headers(CACHE_CONTROL)
    fun createBuySaleAd(@Body saleCreateBody: SaleCreateBody): Call<BuySaleResponse>

    @POST("$EDIT_AD")
    @Headers(CACHE_CONTROL)
    fun editBuySaleAd(@Body saleCreateBody: SaleCreateBody): Call<CommonResponse>

    @POST("$GET_BUY_SALE_FAV")
    @Headers(CACHE_CONTROL)
    fun accessBuySaleFav(@Body buySaleFavBody: BuySaleFavBody): Call<BuySaleResponse>

    @POST("$GET_BUY_SALE_ANIMAL")
    @Headers(CACHE_CONTROL)
    fun accessBuySaleAnimal(@Body buySaleTypeBody: BuySaleTypeBody): Call<BuySaleResponse>

    @POST("$GET_BUY_SALE_MACHINE")
    @Headers(CACHE_CONTROL)
    fun accessBuySaleMachine(@Body buySaleTypeBody: BuySaleTypeBody): Call<BuySaleResponse>

    @POST("$GET_BUY_SALE_EQUIPMENT")
    @Headers(CACHE_CONTROL)
    fun accessBuySaleEquipment(@Body buySaleTypeBody: BuySaleTypeBody): Call<BuySaleResponse>

    @POST("$GET_DIRECTORY{villageId}")
    @Headers(CACHE_CONTROL)
    fun aceesDirectoryData(@Path("villageId") villageId: String): Call<DirectoryListResponse>

    @Multipart
    @POST(UPLOAD_EVENT)
    fun uploadEvent(@Part file: MultipartBody.Part): Call<UploadFile>

    @Multipart
    @POST(UPLOAD_NEWS)
    fun uploadNews(@Part file: MultipartBody.Part): Call<UploadFile>

    @Multipart
    @POST(UPLOAD_SALE)
    fun uploadSale(@Part file: MultipartBody.Part): Call<UploadFile>

    @Multipart
    @POST(UPLOAD_AVATAR)
    fun uploadAvatar(@Part file: MultipartBody.Part): Call<UploadFile>

    @POST("$MY_AD{userId}")
    @Headers(CACHE_CONTROL)
    fun accessMyAds(@Path("userId") userId: String): Call<MyAdResponse>

    @POST("$MY_SALE_AD{userId}")
    @Headers(CACHE_CONTROL)
    fun accessMySaleAds(@Path("userId") userId: String): Call<MySaleAdResponse>

    @GET("$MY_SALE_AD_DELETE{saleId}")
    fun deleteMySaleAd(@Path("saleId") saleId: String): Call<CommonResponse>

    @GET("$MY_SALE_AD_SOLD{saleId}")
    fun markAsSold(@Path("saleId") saleId: String): Call<CommonResponse>

    @POST("$MY_NEWS{userId}")
    @Headers(CACHE_CONTROL)
    fun accessMyNews(@Path("userId") userId: String): Call<MyNewsResponse>

    @GET("$MY_AD_DELETE{eventId}")
    fun deleteMyAd(@Path("eventId") userId: String): Call<CommonResponse>

    @POST("$FAV_DELETE")
    fun deleteFav(@Body addFavBody: AddFavBody): Call<CommonResponse>

    @GET("$MY_NEWS_DELETE{newsId}")
    fun deleteMyNews(@Path("newsId") newsId: String): Call<CommonResponse>

    @POST(CREATE_EVENT)
    @Headers(JSON_TYPE)
    fun createEvent(@Body createAdBody: CreateAdBody): Call<SaveEventResponse>

    @POST(MYAD_EDIT)
    fun editMyAd(@Body createAdBody: CreateAdBody): Call<CommonResponse>

    @GET("$MY_NOTIFICATION{userId}")
    fun accessMyNotification(@Path("userId") userId: String): Call<NotificationListResponse>

    @POST(NOTIFICATION_STATUS)
    fun updateNotificationStatus(@Body notiStatusBody: NotiStatusBody): Call<CommonResponse>

    @POST(NOTIFICATION_ALL)
    fun accessNotification(@Body notificationBody: NotificationBody): Call<NotificationResponse>

    @POST(REQUEST_OTP)
    @Headers(JSON_TYPE)
    fun requestOtp(@Body requestOtp: RequestOtp): Call<OtpResponse>

    @POST(VERIFY_MOBILE)
    @Headers(JSON_TYPE)
    fun verifyMobile(@Body verifyMobile: VerifyMobile): Call<CommonResponse>

    @POST(PROFILE_EDIT)
    @Headers(JSON_TYPE)
    fun editProfile(@Body profileUpdateBody: ProfileUpdateBody): Call<SignInResponse>

    @GET("$GET_EVENT_MEDIA{eventId}")
    fun getEventMedia(@Path("eventId") eventId: String): Call<EventMediaResp>

    @POST("$GET_NEWS_MEDIA{newsId}")
    @Headers(CACHE_CONTROL)
    fun getNewsMedia(@Path("newsId") newsId: String): Call<EventMediaResp>

    @POST("$GET_BUYSALE_MEDIA")
    fun getBuySaleMedia(@Body buySaleMediaBody: BuySaleMediaBody): Call<BuySaleMedia>

    @GET(GET_EVENT_MATTER)
    fun getEventMatter(): Call<ResponseEventMatter>

    @GET(GET_NEWS_MATTER)
    fun getNewsMatter(): Call<ResponseNewsMatter>

    @POST(CREATE_NEWS)
    @Headers(JSON_TYPE)
    fun createNews(@Body createNewsBody: CreateNewsBody): Call<CommonResponse>

    @POST(EDIT_NEWS)
    @Headers(JSON_TYPE)
    fun editNews(@Body createNewsBody: CreateNewsBody): Call<CommonResponse>

    @POST(CREATE_DIR)
    @Headers(JSON_TYPE,CACHE_CONTROL)
    fun createDirectory(@Body createDirectoryBody: CreateDirectoryBody): Call<CommonResponse>

    @POST("$GET_MY_DIR{userId}")
    @Headers(CACHE_CONTROL)
    fun getMyDir(@Path("userId") userId: String): Call<MyDirectoryResponse>

}
