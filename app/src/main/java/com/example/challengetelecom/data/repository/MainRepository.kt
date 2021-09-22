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

    suspend fun getCharacters(page: Int): Flow<Resource<List<Character>>> {
        return flow {
            try {
                val response = appService.getCharacters(page)
                val characters = response.results
                emit(Resource.Success(characters))
            } catch (e: Exception) {
                emit(Resource.Error<List<Character>>(ApiErrorParser(application).parseError(e)))
            }
        }
    }

    suspend fun getCharacter(id: Int): Flow<Resource<Character>> {
        return flow {
            try {
                val response = appService.getCharacter(id)
                emit(Resource.Success(response))
            } catch (e: Exception) {
                emit(Resource.Error<Character>(ApiErrorParser(application).parseError(e)))
            }
        }
    }
}