package com.wildan.storeapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildan.storeapp.MyApp
import com.wildan.storeapp.model.LoginRequest
import com.wildan.storeapp.model.ProductResponse
import com.wildan.storeapp.network.RetrofitClient
import com.wildan.storeapp.repository.ProductRepository
import com.wildan.storeapp.utils.Constant
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

    private val _getDataLogin = MutableLiveData<String?>()
    val getDataLogin: LiveData<String?> = _getDataLogin

    private val _getProductList = MutableLiveData<List<ProductResponse>>()
    val getProductList: LiveData<List<ProductResponse>> = _getProductList

    private val _getProductDetail = MutableLiveData<ProductResponse>()
    val getProductDetail: LiveData<ProductResponse> = _getProductDetail

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

    fun requestLogin(context: Context, body: LoginRequest, isRemember: Boolean) {
        viewModelScope.launch {
            try {
                repository.requestLogin(body)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        MyApp.getInstance().saveAuthDataStore(
                            context, Constant.SAVE_USERNAME,
                            body.password
                        )

                        MyApp.getInstance().saveAuthDataStore(
                            context, Constant.SAVE_PASSWORD,
                            body.password
                        )

                        MyApp.getInstance().saveRememberDataStore(
                            context, Constant.IS_REMEMBER_LOGIN,
                            isRemember
                        )

                        _getDataLogin.value = it.token
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun getProductDetail(id: String) {
        viewModelScope.launch {
            try {
                repository.getProductDetail(id)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getProductDetail.value = it
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