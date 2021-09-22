package com.example.challengetelecom.data.repository

import android.app.Application
import com.example.challengetelecom.data.model.Character
import com.example.challengetelecom.data.network.ApiErrorParser
import com.example.challengetelecom.data.network.Resource
import com.example.challengetelecom.data.network.service.AppService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val application: Application,
    private val appService: AppService
) {

    private var page = 1

    suspend fun getSearchPost(): Flow<Resource<List<Character>>> {
        return flow {
            try {
                val response = appService.getCharacters(page)
                val characters = response.results
                page++
                emit(Resource.Success(characters))
            } catch (e: Exception) {
                emit(Resource.Error<List<Character>>(ApiErrorParser(application).parseError(e)))
            }
        }
    }
}