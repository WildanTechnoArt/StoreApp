package com.wildan.storeapp.model

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    var id: Int?,
    var title: String? = null,
    var price: Double? = null,
    var description: String? = null,
    var category: String? = null,
    var image: String? = null,
    var quantity: Int? = null,
    var rating: Rating? = null
)

data class Rating(
    @SerializedName("rate") var rate: Double? = null,
    @SerializedName("count") var count: Int? = null
)