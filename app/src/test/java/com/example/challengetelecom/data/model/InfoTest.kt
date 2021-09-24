package com.example.challengetelecom.data.model

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class InfoTest {

    private lateinit var info: Info

    @Before
    fun setUp() {
        info = Info("Interdimensional Cable", "https://rickandmortyapi.com/api/location/6")
    }

    @Test
    fun getName() {
        assertEquals("Interdimensional Cable", info.name)
    }

}