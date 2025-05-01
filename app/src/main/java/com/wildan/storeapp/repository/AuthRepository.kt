package com.wildan.storeapp.repository

import com.wildan.storeapp.model.LoginRequest
import com.wildan.storeapp.model.RegisterRequest
import com.wildan.storeapp.data.api.BaseApiService
import kotlinx.coroutines.flow.flow

class AuthRepository(private val baseApi: BaseApiService) {

    fun requestLogin(body: LoginRequest) = flow {
        val response = baseApi.requestLogin(body)
        emit(response)
    }

    fun registerUser(body: RegisterRequest) = flow {
        val response = baseApi.registerUser(body)
        emit(response)
    }
}