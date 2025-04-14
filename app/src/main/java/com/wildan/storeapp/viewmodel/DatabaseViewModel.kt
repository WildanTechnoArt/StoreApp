package com.wildan.storeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildan.storeapp.database.DatabaseRepository
import com.wildan.storeapp.model.ProductResponse
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: DatabaseRepository) : ViewModel() {

    val allData: LiveData<List<ProductResponse>> = repository.getListCart()
    val getTotalItemCount: LiveData<Int> = repository.getTotalItemCount()

    val isAddCart: LiveData<Boolean> = repository.isAddCart

    fun checkIfAddCart(id: String) {
        repository.checkIfAddCart(id)
    }

    fun toggleChart(data: ProductResponse, callback: (Boolean) -> Unit) {
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