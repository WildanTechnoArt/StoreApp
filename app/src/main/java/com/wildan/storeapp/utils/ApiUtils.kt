package com.wildan.storeapp.utils

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import com.wildan.storeapp.R
import com.wildan.storeapp.model.Response
import com.wildan.storeapp.extensions.showToast
import com.wildan.storeapp.utils.NetworkUtil.isConnected
import retrofit2.HttpException
import java.net.SocketTimeoutException

fun FragmentActivity.handleErrorApi(error: Throwable) {
    if (isConnected(this)) {
        when (error) {
            is HttpException -> {
                try {
                    val errorBody = error.response()?.errorBody()?.string()
                    if (errorBody?.startsWith("{") == true) {
                        val response = Gson().fromJson(errorBody,
                            Response::class.java
                        )
                        handleSessionAndException(error, response?.message)
                    } else {
                        showToast(errorBody ?: "Unknown error")
                    }
                } catch (e: Exception) {
                    e.localizedMessage?.let { showToast(it) }
                }
            }

            is SocketTimeoutException -> showToast("Connection Timeout!")
            else -> showToast(error.toString())
        }
    } else {
        showToast(getString(R.string.message_if_disconnect))
    }
}

fun <T, VH : RecyclerView.ViewHolder> handleData(
    page: Int?,
    data: List<T>?,
    adapter: ListAdapter<T, VH>,
    recyclerView: RecyclerView?,
    emptyTextView: MaterialTextView?
) {
    val newData = data?.let {
        if (page != null && page > 1) {
            adapter.currentList + it
        } else {
            it
        }
    } ?: emptyList()

    adapter.submitList(newData) {
        if (page != null && page > 1 && data?.isNotEmpty() == true) {
            recyclerView?.smoothScrollToPosition(adapter.itemCount - data.size)
        }
    }

    val isEmpty = data.isNullOrEmpty()
    recyclerView?.visibility = if (isEmpty) View.GONE else View.VISIBLE
    emptyTextView?.visibility = if (isEmpty) View.VISIBLE else View.GONE
}

private fun FragmentActivity.handleSessionAndException(
    exception: HttpException,
    message: String?
) {
    if (exception.code() == 401) {
        showToast("Sorry your session has ended")
        finishAffinity()
    } else {
        if (!message.isNullOrBlank() && message != "null") {
            showToast(message)
        }
    }
}