package com.gavkariapp.network

import android.util.Log
import com.gavkariapp.constant.HttpConstant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


object ApiClient {


    fun get(): Retrofit {

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                //print input
                .client(OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                            Log.w("ApiCall=", message)
                        }).setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())

                .baseUrl(BASE_URL)
                //execute onClickRow back in background thread
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build()
    }

}
