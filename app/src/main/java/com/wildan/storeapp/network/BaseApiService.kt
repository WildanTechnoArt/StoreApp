package com.wildan.storeapp.network

import com.wildan.storeapp.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface BaseApiService {

    @Headers("Accept: application/json")
    @GET("products/categories")
    suspend fun getCategoryList(): List<String>

    @Headers("Accept: application/json")
    @GET("products")
    suspend fun getProductList(): List<ProductResponse>
}