package com.example.challengetelecom.data.network.service

import com.example.challengetelecom.data.network.response.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {

    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): CharacterResponse
}