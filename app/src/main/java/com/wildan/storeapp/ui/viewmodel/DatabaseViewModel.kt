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

    val isAddCart: LiveData<Boolean> = repository.isAddCart

    fun checkIfAddCart(id: String) {
        repository.checkIfAddCart(id)
    }

    fun toggleChart(data: ProductEntity, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (isAddCart.value == true) {
                repository.removeCart(data)
                callback(false)
            } else {
                repository.addCart(data)
                callback(true)
            }
        }
    }
}