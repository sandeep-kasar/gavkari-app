package com.gavkariapp.Model


data class ApiResponse<R, E>(var response: R?, var error: E?)