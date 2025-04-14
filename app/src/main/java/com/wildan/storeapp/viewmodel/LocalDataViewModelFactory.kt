package com.wildan.storeapp.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wildan.storeapp.database.DatabaseRepository

class LocalDataViewModelFactory private constructor(private val repository: DatabaseRepository) :
    ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: LocalDataViewModelFactory? = null

        fun getInstance(context: FragmentActivity): LocalDataViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LocalDataViewModelFactory(
                    DatabaseRepository.getInstance(context)
                ).also { instance = it }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(DatabaseViewModel::class.java) -> {
                DatabaseViewModel(repository) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}