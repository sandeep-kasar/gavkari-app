package com.gavkariapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gavkariapp.Model.*
import com.gavkariapp.constant.HttpConstant
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeRepository {

    /**
     * access my village data api onClickRow
     */
    fun accessMyVillageData(eventFilterBody: EventFilterBody): LiveData<ApiResponse<MyVillageResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<MyVillageResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .aceesMyVillageData(eventFilterBody)
                .enqueue(object : Callback<MyVillageResponse> {
                    override fun onResponse(call: Call<MyVillageResponse>?, response: Response<MyVillageResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<MyVillageResponse>?, t: Throwable?) {

                        //return error response to view model
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access news data api onClickRow
     */
    fun accessNewsData(newsFilterBody: NewsFilterBody): LiveData<ApiResponse<NewsResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<NewsResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessNews(newsFilterBody)
                .enqueue(object : Callback<NewsResponse> {
                    override fun onResponse(call: Call<NewsResponse>?, response: Response<NewsResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<NewsResponse>?, t: Throwable?) {

                        //return error response to view model
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access buy sale data api onClickRow
     */
    fun accessBuySaleData(buySaleBody: BuySaleBody): LiveData<ApiResponse<BuySaleResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<BuySaleResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessBuySale(buySaleBody)
                .enqueue(object : Callback<BuySaleResponse> {
                    override fun onResponse(call: Call<BuySaleResponse>?, response: Response<BuySaleResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<BuySaleResponse>?, t: Throwable?) {

                        //return error response to view model
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access buy sale animal data api onClickRow
     */
    fun accessBuySaleAnimal(buySaleTypeBody: BuySaleTypeBody): LiveData<ApiResponse<BuySaleResponse, String>> {
        val responseData: MutableLiveData<ApiResponse<BuySaleResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessBuySaleAnimal(buySaleTypeBody)
                .enqueue(object : Callback<BuySaleResponse> {
                    override fun onResponse(call: Call<BuySaleResponse>?, response: Response<BuySaleResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }
                    override fun onFailure(call: Call<BuySaleResponse>?, t: Throwable?) {
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access buy sale machine data api onClickRow
     */
    fun accessBuySaleMachine(buySaleTypeBody: BuySaleTypeBody): LiveData<ApiResponse<BuySaleResponse, String>> {
        val responseData: MutableLiveData<ApiResponse<BuySaleResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessBuySaleMachine(buySaleTypeBody)
                .enqueue(object : Callback<BuySaleResponse> {
                    override fun onResponse(call: Call<BuySaleResponse>?, response: Response<BuySaleResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }
                    override fun onFailure(call: Call<BuySaleResponse>?, t: Throwable?) {
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access buy sale equipment data api onClickRow
     */
    fun accessBuySaleEquipment(buySaleTypeBody: BuySaleTypeBody): LiveData<ApiResponse<BuySaleResponse, String>> {
        val responseData: MutableLiveData<ApiResponse<BuySaleResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessBuySaleEquipment(buySaleTypeBody)
                .enqueue(object : Callback<BuySaleResponse> {
                    override fun onResponse(call: Call<BuySaleResponse>?, response: Response<BuySaleResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }
                    override fun onFailure(call: Call<BuySaleResponse>?, t: Throwable?) {
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access buy sale fav data api onClickRow
     */
    fun accessBuySaleFavData(buySaleFavBody: BuySaleFavBody): LiveData<ApiResponse<BuySaleResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<BuySaleResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessBuySaleFav(buySaleFavBody)
                .enqueue(object : Callback<BuySaleResponse> {
                    override fun onResponse(call: Call<BuySaleResponse>?, response: Response<BuySaleResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<BuySaleResponse>?, t: Throwable?) {

                        //return error response to view model
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access directory list api onClickRow
     */
    fun accessDirectoryData(villageId: String): LiveData<ApiResponse<DirectoryListResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<DirectoryListResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .aceesDirectoryData(villageId)
                .enqueue(object : Callback<DirectoryListResponse> {
                    override fun onResponse(call: Call<DirectoryListResponse>?, response: Response<DirectoryListResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<DirectoryListResponse>?, t: Throwable?) {
                        //return error response to view model
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access my ad data api onClickRow
     */
    fun accessMyAdData(userId: String): LiveData<ApiResponse<MyAdResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<MyAdResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessMyAds(userId)
                .enqueue(object : Callback<MyAdResponse> {
                    override fun onResponse(call: Call<MyAdResponse>?, response: Response<MyAdResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<MyAdResponse>?, t: Throwable?) {
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access my sale ad data api onClickRow
     */
    fun accessMySaleAdData(userId: String): LiveData<ApiResponse<MySaleAdResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<MySaleAdResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessMySaleAds(userId)
                .enqueue(object : Callback<MySaleAdResponse> {
                    override fun onResponse(call: Call<MySaleAdResponse>?, response: Response<MySaleAdResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<MySaleAdResponse>?, t: Throwable?) {
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access my news data api onClickRow
     */
    fun accessMyNewsData(userId: String): LiveData<ApiResponse<MyNewsResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<MyNewsResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessMyNews(userId)
                .enqueue(object : Callback<MyNewsResponse> {
                    override fun onResponse(call: Call<MyNewsResponse>?, response: Response<MyNewsResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<MyNewsResponse>?, t: Throwable?) {
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access my notification
     */
    fun accessNotification(userId: String): LiveData<ApiResponse<NotificationListResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<NotificationListResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessMyNotification(userId)
                .enqueue(object : Callback<NotificationListResponse> {
                    override fun onResponse(call: Call<NotificationListResponse>?, response: Response<NotificationListResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<NotificationListResponse>?, t: Throwable?) {
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }

    /**
     * access notification
     */
    fun accessReceivedNotification(notificationBody: NotificationBody): LiveData<ApiResponse<NotificationResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<NotificationResponse, String>> = MutableLiveData()
        ApiClient.get().create(ApiInterface::class.java)
                .accessNotification(notificationBody)
                .enqueue(object : Callback<NotificationResponse> {
                    override fun onResponse(call: Call<NotificationResponse>?, response: Response<NotificationResponse>?) {
                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response.body(), null))
                        }
                    }

                    override fun onFailure(call: Call<NotificationResponse>?, t: Throwable?) {
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }
                })

        return responseData
    }
}


