package com.wildan.storeapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "TableProducts"
)
data class ProductResponse(
    @PrimaryKey
    @ColumnInfo("id") var id: Int? = null,
    @ColumnInfo("title") var title: String? = null,
    @ColumnInfo("price") var price: Double? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("category") var category: String? = null,
    @ColumnInfo("image") var image: String? = null,
    @ColumnInfo("quantity") var count: Int? = null,
    @SerializedName("rating") var rating: Rating? = Rating()
) : Parcelable

@Parcelize
data class Rating(
    @SerializedName("rate") var rate: Double? = null,
    @SerializedName("count") var count: Int? = null
) : Parcelable