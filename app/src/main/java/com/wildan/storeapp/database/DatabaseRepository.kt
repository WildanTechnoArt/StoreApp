package com.wildan.storeapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.wildan.storeapp.model.ProductResponse

class DatabaseRepository private constructor(private val dataDao: DataDao) {

    private val _isAddCart = MediatorLiveData<Boolean>()
    val isAddCart: LiveData<Boolean> get() = _isAddCart

    private var lastSource: LiveData<ProductResponse?>? = null

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

    fun getListCart(): LiveData<List<ProductResponse>> = dataDao.getAllCart()
    fun getTotalItemCount(): LiveData<Int> = dataDao.getTotalItemCount()

    suspend fun addCart(data: ProductResponse) = dataDao.addCart(data)

    suspend fun removeCart(data: ProductResponse) = dataDao.removeCart(data)

    fun checkIfAddCart(id: String) {
        lastSource?.let { _isAddCart.removeSource(it) }

        val source = dataDao.isProductCart(id)
        lastSource = source

        _isAddCart.addSource(source) { cart ->
            _isAddCart.value = cart != null
        }
    }
}