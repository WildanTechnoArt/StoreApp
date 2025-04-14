package com.wildan.storeapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wildan.storeapp.model.ProductResponse

@Dao
interface DataDao {

    @Query("SELECT * FROM TableProducts ORDER BY id ASC")
    fun getAllCart(): LiveData<List<ProductResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCart(orders: ProductResponse): Long

    @Delete
    suspend fun removeCart(data: ProductResponse)

    @Query("SELECT * FROM TableProducts WHERE url = :url LIMIT 1")
    fun isProductCart(url: String): LiveData<ProductResponse?>
}