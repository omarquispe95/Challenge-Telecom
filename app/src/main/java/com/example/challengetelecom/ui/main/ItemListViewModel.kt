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
class ItemListViewModel @Inject constructor(
    application: Application,
    private val repository: MainRepository,
) : AndroidViewModel(application) {

    private val _charactersLiveData = MutableLiveData<Resource<List<Character>>>()
    fun getCharactersLiveData(): LiveData<Resource<List<Character>>> = _charactersLiveData

    init {
        getCharacters()
    }

    fun getCharacters() {
        _charactersLiveData.postValue(Resource.Loading())
        viewModelScope.launch {
            repository.getSearchPost().collect { _charactersLiveData.postValue(it) }
        }
    }
}