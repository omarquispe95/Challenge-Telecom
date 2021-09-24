package com.example.challengetelecom.util

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

fun View.visible() {
    if (isVisible) return
    visibility = View.VISIBLE
}

fun View.gone() {
    if (isGone) return
    visibility = View.GONE
}

fun RecyclerView.isFirstItem(view: View): Boolean {
    return getChildAdapterPosition(view) == 0
}