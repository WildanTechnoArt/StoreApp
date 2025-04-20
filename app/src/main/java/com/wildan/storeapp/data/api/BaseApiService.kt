package com.wildan.storeapp.data.api

import com.wildan.storeapp.model.LoginRequest
import com.wildan.storeapp.model.LoginResponse
import com.wildan.storeapp.model.ProductResponse
import com.wildan.storeapp.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface BaseApiService {

    @Headers("Accept: application/json")
    @GET("products/categories")
    suspend fun getCategoryList(): List<String>

    @Headers("Accept: application/json")
    @GET("products")
    suspend fun getProductList(): List<ProductResponse>

    @Headers("Accept: application/json")
    @GET("products/category/{category}")
    suspend fun getProductByCategory(
        @Path("category") category: String
    ): List<ProductResponse>

    @Headers("Accept: application/json")
    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") id: String?,
    ): ProductResponse

    @Headers("Accept: application/json")
    @POST("auth/login")
    suspend fun requestLogin(
        @Body body: LoginRequest
    ): LoginResponse

    @Headers("Accept: application/json")
    @POST("users")
    suspend fun registerUser(
        @Body body: RegisterRequest
    ): RegisterRequest
}