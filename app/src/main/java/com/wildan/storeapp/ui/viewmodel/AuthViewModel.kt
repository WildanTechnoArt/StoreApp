package com.wildan.storeapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wildan.storeapp.model.LoginRequest
import com.wildan.storeapp.model.RegisterRequest
import com.wildan.storeapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _getDataLogin = MutableLiveData<String?>()
    val getDataLogin: LiveData<String?> = _getDataLogin

    private val _successRegister = MutableLiveData<String?>()
    val successRegister: LiveData<String?> = _successRegister

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    fun requestLogin(body: LoginRequest) {
        viewModelScope.launch {
            try {
                repository.requestLogin(body)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _getDataLogin.value = it.token
                    }
            } catch (e: Exception) {
                errorHandle(e)
            }
        }
    }

    fun registerUser(body: RegisterRequest) {
        viewModelScope.launch {
            try {
                repository.registerUser(body)
                    .flowOn(Dispatchers.IO)
                    .onStart { _loading.value = true }
                    .onCompletion { _loading.postValue(false) }
                    .catch { errorHandle(it) }
                    .collect {
                        _successRegister.value = "Register Success"
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