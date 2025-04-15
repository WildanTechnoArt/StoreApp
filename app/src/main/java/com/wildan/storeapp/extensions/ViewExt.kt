package com.wildan.storeapp.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.setOnSingleClickListener(interval: Long = 600L, action: (View) -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        if (System.currentTimeMillis() - lastClickTime < interval) return@setOnClickListener
        lastClickTime = System.currentTimeMillis()
        action(it)
    }
}