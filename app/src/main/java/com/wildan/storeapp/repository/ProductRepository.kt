package com.wildan.storeapp.repository

import com.wildan.storeapp.network.BaseApiService
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
}