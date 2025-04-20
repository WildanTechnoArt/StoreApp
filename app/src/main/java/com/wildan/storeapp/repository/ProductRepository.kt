package com.wildan.storeapp.repository

import com.wildan.storeapp.model.LoginRequest
import com.wildan.storeapp.model.RegisterRequest
import com.wildan.storeapp.data.api.BaseApiService
import kotlinx.coroutines.flow.flow

class ProductRepository(private val baseApi: BaseApiService) {

    fun getCategoryList() = flow {
        val response = baseApi.getCategoryList()
        emit(response)
    }

    fun getProductList() = flow {
        val response = baseApi.getProductList()
        emit(response)
    }

    fun getProductByCategory(category: String) = flow {
        val response = baseApi.getProductByCategory(category)
        emit(response)
    }

    fun getProductDetail(id: String) = flow {
        val response = baseApi.getProductDetail(id)
        emit(response)
    }

    fun requestLogin(body: LoginRequest) = flow {
        val response = baseApi.requestLogin(body)
        emit(response)
    }

    fun registerUser(body: RegisterRequest) = flow {
        val response = baseApi.registerUser(body)
        emit(response)
    }
}