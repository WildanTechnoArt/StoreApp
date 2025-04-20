package com.wildan.storeapp.data.database

import android.content.Context
import androidx.lifecycle.LiveData

class DatabaseRepository private constructor(private val dataDao: DataDao) {

    companion object {
        @Volatile
        private var instance: DatabaseRepository? = null

        fun getInstance(context: Context): DatabaseRepository {
            return instance ?: synchronized(this) {
                instance ?: DatabaseRepository(AppDatabase.getInstance(context).dataDao())
                    .also { instance = it }
            }
        }
    }

    fun getListCart(): LiveData<List<ProductEntity>> = dataDao.getAllCart()
    fun getTotalItemCount(): LiveData<Int> = dataDao.getTotalItemCount()

    suspend fun addCart(data: ProductEntity) = dataDao.addCart(data)

    suspend fun clearCart() = dataDao.clearCart()

    suspend fun checkIfAddCart(id: Int?) = dataDao.isProductCart(id)

    suspend fun insertQuantity(productId: Int?, qty: Int) = dataDao.insertQuantity(productId, qty)

    suspend fun removeCart(data: ProductEntity) = dataDao.removeCart(data)
}