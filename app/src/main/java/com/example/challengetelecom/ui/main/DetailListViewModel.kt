package com.example.challengetelecom.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengetelecom.data.model.Character
import com.example.challengetelecom.data.network.Resource
import com.example.challengetelecom.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailListViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository,
) : AndroidViewModel(application) {

    private val _characterLiveData = MutableLiveData<Resource<Character>>()
    fun getCharacterLiveData(): LiveData<Resource<Character>> = _characterLiveData

    fun getCharacters(id: Int) {

        _characterLiveData.postValue(Resource.Loading())
        viewModelScope.launch {
            repository.getCharacter(id).collect {
                _characterLiveData.postValue(it)
            }
        }
    }
}