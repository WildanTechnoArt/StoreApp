package com.wildan.storeapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") var token: String? = null
)