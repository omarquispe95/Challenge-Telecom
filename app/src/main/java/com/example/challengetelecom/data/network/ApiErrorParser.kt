package com.example.challengetelecom.data.network

import android.content.Context
import com.example.challengetelecom.R
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiErrorParser(private val context: Context) {
    fun parseError(error: Throwable): String {
        return when (error) {
            is HttpException -> {
                when(error.code()) {
                    in 400..499 -> {
                        context.getString(R.string.error_generic)
                    }
                    in 500..599 -> {
                        context.getString(R.string.error_server)
                    }
                    else -> context.getString(R.string.error_unknown)
                }
            }
            is SocketTimeoutException -> context.getString(R.string.error_time_out)
            is UnknownHostException -> context.getString(R.string.error_no_connection_internet)
            is JsonParseException -> context.getString(R.string.error_generic)
            is IOException -> context.getString(R.string.error_generic)
            else -> context.getString(R.string.error_generic)
        }
    }
}