package com.wildan.storeapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataDao {

    @Query("SELECT * FROM TableProducts ORDER BY id ASC")
    fun getAllCart(): LiveData<List<ProductEntity>>

    @Query("SELECT SUM(quantity) FROM TableProducts")
    fun getTotalQuantity(): LiveData<Int>

    @Query("SELECT count(*) FROM TableProducts")
    fun getTotalItemCount(): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCart(orders: ProductEntity): Long

    @Delete
    suspend fun removeCart(data: ProductEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM TableProducts WHERE id = :id LIMIT 1)")
    suspend fun isProductCart(id: Int?): Boolean

    @Query("UPDATE TableProducts SET quantity = quantity + :qty WHERE id = :productId")
    suspend fun insertQuantity(productId: Int?, qty: Int)
}