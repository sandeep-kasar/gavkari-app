package com.gavkariapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gavkariapp.Model.*
import com.gavkariapp.activity.NotificationActivity
import com.gavkariapp.repository.HomeRepository


class HomeViewModel : ViewModel() {

    private val homeRepository = HomeRepository()

    val buySaleResponse = MutableLiveData<BuySaleResponse>()

    val designResponse = MutableLiveData<Design>()

    val isInternetAvail = MutableLiveData<Boolean>()

    /**
     * access my village data
     */
    fun getMyVillageCall(eventFilterBody: EventFilterBody): LiveData<ApiResponse<MyVillageResponse, String>> {
        return homeRepository.accessMyVillageData(eventFilterBody)
    }

    /**
     * access News data
     */
    fun getNewsData(newsFilterBody: NewsFilterBody): LiveData<ApiResponse<NewsResponse, String>> {
        return homeRepository.accessNewsData(newsFilterBody)
    }

    /**
     * access Buy Sale Data
     */
    fun getBuySaleData(buySaleBody: BuySaleBody): LiveData<ApiResponse<BuySaleResponse, String>> {
        return homeRepository.accessBuySaleData(buySaleBody)
    }

    /**
     * access Buy Sale Animal Data
     */
    fun accessBuySaleAnimal(buySaleTypeBody: BuySaleTypeBody): LiveData<ApiResponse<BuySaleResponse, String>> {
        return homeRepository.accessBuySaleAnimal(buySaleTypeBody)
    }

    /**
     * access Buy Sale Machine Data
     */
    fun accessBuySaleMachine(buySaleTypeBody: BuySaleTypeBody): LiveData<ApiResponse<BuySaleResponse, String>> {
        return homeRepository.accessBuySaleMachine(buySaleTypeBody)
    }

    /**
     * access Buy Sale Equipment Data
     */
    fun accessBuySaleEquipment(buySaleTypeBody: BuySaleTypeBody): LiveData<ApiResponse<BuySaleResponse, String>> {
        return homeRepository.accessBuySaleEquipment(buySaleTypeBody)
    }

    /**
     * access Buy Sale fav
     */
    fun getbuySaleFavData(buySaleFavBody: BuySaleFavBody): LiveData<ApiResponse<BuySaleResponse, String>> {
        return homeRepository.accessBuySaleFavData(buySaleFavBody)
    }

    /**
     * access village directory
     */
    fun accessDirectoryCall(villageId: String): LiveData<ApiResponse<DirectoryListResponse, String>> {
        return homeRepository.accessDirectoryData(villageId)
    }

    /**
     * access my ads
     */
    fun accessMyAdsCall(userId: String): LiveData<ApiResponse<MyAdResponse, String>> {
        return homeRepository.accessMyAdData(userId)
    }

    /**
     * access sale my ads
     */
    fun accessMySaleAdData(userId: String): LiveData<ApiResponse<MySaleAdResponse, String>> {
        return homeRepository.accessMySaleAdData(userId)
    }

    /**
     * access my news
     */
    fun accessMyNewsCall(userId: String): LiveData<ApiResponse<MyNewsResponse, String>> {
        return homeRepository.accessMyNewsData(userId)
    }

    /**
     * access my notification
     */
    fun accessMyNotification(userId: String): LiveData<ApiResponse<NotificationListResponse, String>> {
        return homeRepository.accessNotification(userId)
    }

    /**
     * access notification
     */
    fun accessNotification(notificationBody: NotificationBody): LiveData<ApiResponse<NotificationResponse, String>> {
        return homeRepository.accessReceivedNotification(notificationBody)
    }


}
