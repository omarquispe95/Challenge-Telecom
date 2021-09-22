package com.example.challengetelecom.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.isFirstItem(view: View): Boolean {
    return getChildAdapterPosition(view) == 0
}