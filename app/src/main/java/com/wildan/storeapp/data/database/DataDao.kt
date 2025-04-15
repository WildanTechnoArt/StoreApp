package com.wildan.storeapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wildan.storeapp.model.ProductResponse

@Dao
interface DataDao {

    @Query("SELECT * FROM TableProducts ORDER BY id ASC")
    fun getAllCart(): LiveData<List<ProductResponse>>

    @Query("SELECT SUM(quantity) FROM TableProducts")
    fun getTotalItemCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCart(orders: ProductResponse): Long

    @Delete
    suspend fun removeCart(data: ProductResponse)

    @Query("SELECT * FROM TableProducts WHERE id = :id LIMIT 1")
    fun isProductCart(id: String): LiveData<ProductResponse?>
}