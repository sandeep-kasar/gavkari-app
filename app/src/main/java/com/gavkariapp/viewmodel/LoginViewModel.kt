package com.gavkariapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gavkariapp.Model.*
import com.gavkariapp.repository.LoginRepository


class LoginViewModel : ViewModel() {

    private val loginRepository = LoginRepository()

    /**
     * user login  api onClickRow
     */
    fun signInCall(userBody: SignInBody): LiveData<ApiResponse<SignInResponse, String>> {
        return loginRepository.signInUser(userBody)
    }

    /**
     * onClickRow api from repository for registration
     * params user body
     */
    fun signUpCall(userBody: SignUpBody): LiveData<ApiResponse<SignInResponse, String>> {
        return loginRepository.signUpUser(userBody)
    }
}
