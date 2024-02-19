package com.basaran.burakbasarancase.common.util

import android.view.View

fun View.showProgress() {
    this.visibility = View.VISIBLE
}

fun View.hideProgress() {
    this.visibility = View.GONE
}