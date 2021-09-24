package com.example.challengetelecom.data.network

import com.example.challengetelecom.data.model.Info
import org.junit.Before
import org.junit.Test

class ResourceTest {

    private lateinit var resource1: Resource<Info>
    private lateinit var resource2: Resource<Info>
    private lateinit var resource3: Resource<Info>
    private lateinit var origin1: Info
    private lateinit var origin2: Info

    @Before
    fun setUp() {
        origin1 = Info("Interdimensional Cable", "https://rickandmortyapi.com/api/location/6")
        origin2 = Info("Interdimensional", "https://rickandmortyapi.com/api/location/6")
        resource1 = Resource.Success(origin1)
        resource2 = Resource.Error(null, origin1)
        resource3 = Resource.Loading()
    }

    @Test
    fun isTypeSuccess() {
        assert(resource1 is Resource.Success)
    }

    @Test
    fun isTypeError() {
        assert(resource2 is Resource.Error)
    }

    @Test
    fun isTypeLoading() {
        assert(resource3 is Resource.Loading)
    }

    @Test
    fun getData() {
        assert(resource1.data is Info)
    }

    @Test
    fun getCompareData() {
        assert(!resource1.data?.name.equals(origin2.name))
    }

    @Test
    fun getMessage() {
        assert(resource2.message == null)
    }
}