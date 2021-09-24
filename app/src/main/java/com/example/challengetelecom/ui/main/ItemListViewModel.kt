package com.example.challengetelecom.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.challengetelecom.R
import com.example.challengetelecom.application.BaseApplication
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

    private var connectivityManager: ConnectivityManager? = null
    private var page = 1
    private val _charactersLiveData = MutableLiveData<Resource<List<Character>>>()
    fun getCharactersLiveData(): LiveData<Resource<List<Character>>> = _charactersLiveData

    init {
        setUpConnectivityManager(application.applicationContext)
    }

    private fun setUpConnectivityManager(applicationContext: Context) {
        connectivityManager = getSystemService(applicationContext, ConnectivityManager::class.java)
        connectivityManager?.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                if (page == 1)
                    getCharacters(false)
            }

            override fun onLost(network: Network) {
                noInternetConnection()
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                //Log.e(this@ItemListViewModel::class.java.simpleName, "The default network changed capabilities: " + networkCapabilities)
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                //Log.e(this@ItemListViewModel::class.java.simpleName, "The default network changed link properties: " + linkProperties)
            }
        })
        if (!hasInternetConnection())
            noInternetConnection()
    }

    fun getCharacters(isReset: Boolean = false) {
        if (isReset)
            page = 1

        _charactersLiveData.postValue(Resource.Loading())
        if (hasInternetConnection())
            viewModelScope.launch {
                repository.getCharacters(page).collect {
                    if (it is Resource.Success)
                        page++

                    _charactersLiveData.postValue(it)
                }
            }
        else
            noInternetConnection()
    }

    private fun noInternetConnection() {
        _charactersLiveData.postValue(
            Resource.Error(
                getApplication<BaseApplication>().applicationContext.getString(
                    R.string.error_no_connection_internet
                )
            )
        )
    }

    private fun hasInternetConnection(): Boolean {
        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val capabilities =
            connectivityManager?.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}