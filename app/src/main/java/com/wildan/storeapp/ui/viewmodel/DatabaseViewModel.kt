package com.wildan.storeapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildan.storeapp.data.database.DatabaseRepository
import com.wildan.storeapp.data.database.ProductEntity
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: DatabaseRepository) : ViewModel() {

    val allData: LiveData<List<ProductEntity>> = repository.getListCart()
    val getTotalItemCount: LiveData<Int> = repository.getTotalItemCount()

    fun removeFromCart(product: ProductEntity){
        viewModelScope.launch {
            repository.removeCart(product)
        }
    }

    fun addToCart(data: ProductEntity, qty: Int) {
        viewModelScope.launch {
            val isAddCart = repository.checkIfAddCart(data.id)
            if (isAddCart) {
                repository.insertQuantity(data.id, qty)
            } else {
                repository.addCart(data)
            }
        }
    }
}