package com.wildan.storeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildan.storeapp.model.ProductResponse
import com.wildan.storeapp.network.RetrofitClient
import com.wildan.storeapp.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repository: ProductRepository by lazy { ProductRepository(RetrofitClient.instance) }

    private val _getCategoryList = MutableLiveData<List<String>>()
    val getCategoryList: LiveData<List<String>> = _getCategoryList

    private val _getProductList = MutableLiveData<List<ProductResponse>>()
    val getProductList: LiveData<List<ProductResponse>> = _getProductList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun getCategoryList() {
        viewModelScope.launch {
            try {
                repository.getCategoryList()
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getCategoryList.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun getProductList() {
        viewModelScope.launch {
            try {
                repository.getProductList()
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getProductList.value = it
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    private fun errorHandle(it: Throwable) {
        _loading.postValue(false)
        _error.postValue(it)
    }
}