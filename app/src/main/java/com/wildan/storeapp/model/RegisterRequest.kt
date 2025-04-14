package com.wildan.storeapp.model

data class RegisterRequest(
    var username: String? = null,
    var email: String? = null,
    var password: String? = null
)