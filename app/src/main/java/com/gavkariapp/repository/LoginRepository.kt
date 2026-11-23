package com.gavkariapp.repository

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gavkariapp.Model.*
import com.gavkariapp.network.ApiClient
import com.gavkariapp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRepository {

    /**
     * login api onClickRow
     */
    fun signInUser(userBody: SignInBody): LiveData<ApiResponse<SignInResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<SignInResponse, String>> = MutableLiveData()

        ApiClient.get().create(ApiInterface::class.java)
                .signIn(userBody)
                .enqueue(object : Callback<SignInResponse> {
                    override fun onResponse(call: Call<SignInResponse>?, response: Response<SignInResponse>?) {

                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response!!.body(), null))
                        } else {
                            //handle error part here
                            if (response.errorBody() != null) {
                                responseData.postValue(ApiResponse(null, response.errorBody()?.string()))
                            }
                        }
                    }

                    override fun onFailure(call: Call<SignInResponse>?, t: Throwable?) {

                        //return error response to view model
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }

                })

        return responseData
    }

    /**
     * register api onClickRow
     */
    fun signUpUser(userBody: SignUpBody): LiveData<ApiResponse<SignInResponse, String>> {

        val responseData: MutableLiveData<ApiResponse<SignInResponse, String>> = MutableLiveData()

        ApiClient.get().create(ApiInterface::class.java)
                .signUp(userBody)
                .enqueue(object : Callback<SignInResponse> {


                    override fun onResponse(call: Call<SignInResponse>?, response: Response<SignInResponse>?) {

                        if (response!!.code() == 200) {
                            responseData.postValue(ApiResponse(response!!.body(), null))
                        } else {
                            //handle error part here
                            if (response.errorBody() != null) {
                                responseData.postValue(ApiResponse(null, response.errorBody()?.string()))

                            }
                        }

                    }

                    override fun onFailure(call: Call<SignInResponse>?, t: Throwable?) {

                        //return error response to view model
                        responseData.postValue(ApiResponse(null, t!!.message))
                    }


                })

        return responseData

    }

}


