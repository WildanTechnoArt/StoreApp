package com.wildan.storeapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "TableProducts"
)
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo("id") var id: Int? = null,
    @ColumnInfo("title") var title: String? = null,
    @ColumnInfo("price") var price: Double? = null,
    @ColumnInfo("image") var image: String? = null,
    @ColumnInfo("quantity") var count: Int? = 0,
)