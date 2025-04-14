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
    @ColumnInfo("description") var description: String? = null,
    @ColumnInfo("category") var category: String? = null,
    @ColumnInfo("image") var image: String? = null,
    @ColumnInfo("rating") var rating: Rating? = Rating()
) : Parcelable

@Parcelize
data class Rating(
    @SerializedName("rate") var rate: Double? = null,
    @SerializedName("count") var count: Int? = null
) : Parcelable