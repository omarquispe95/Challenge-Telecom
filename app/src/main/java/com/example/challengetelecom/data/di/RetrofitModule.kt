package com.example.challengetelecom.data.di

import android.util.Log
import com.example.challengetelecom.data.network.service.AppService
import com.example.challengetelecom.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        val gsonConverterFactory = GsonConverterFactory.create()

        val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
            Log.i("ApiClient", message)
        }

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Constants.TIME_OUT, TimeUnit.SECONDS)


        return Retrofit.Builder().baseUrl(Constants.API_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient.build())
    }

    @Singleton
    @Provides
    fun provideAppService(retrofitBuilder: Retrofit.Builder): AppService {
        return retrofitBuilder
            .build()
            .create(AppService::class.java)
    }
}