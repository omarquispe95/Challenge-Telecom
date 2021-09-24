package com.example.challengetelecom.data.model

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class CharacterTest {

    private lateinit var character1: Character
    private lateinit var character2: Character
    private lateinit var origin1: Info
    private lateinit var location1: Info

    @Before
    fun setUp() {
        origin1 = Info("unknown", "")
        location1 = Info("Interdimensional Cable", "https://rickandmortyapi.com/api/location/6")
        character1 = Character(
            275,
            "Randy Dicknose",
            "Alive",
            "Human",
            "",
            "Male",
            origin1,
            location1,
            "https://rickandmortyapi.com/api/character/avatar/275.jpeg",
            arrayListOf("https://rickandmortyapi.com/api/episode/19"),
            "https://rickandmortyapi.com/api/character/275",
            "2017-12-31T14:16:45.776Z"
        )
        character2 = Character(
            276,
            "Randy Dicknose",
            "Alive",
            "Human",
            "",
            "Male",
            origin1,
            location1,
            "https://rickandmortyapi.com/api/character/avatar/275.jpeg",
            arrayListOf("https://rickandmortyapi.com/api/episode/19"),
            "https://rickandmortyapi.com/api/character/275",
            "2017-12-31T14:16:45.776Z"
        )
    }

    @Test
    fun getId() {
        assertEquals(276, character2.id)
    }

    @Test
    fun checkEquals() {
        assertFalse(character1.equals(character2))
    }

    @Test
    fun getName() {
        assertTrue(character1.name!!.equals(character2.name))
    }

    @Test
    fun getImage() {
        assertEquals("https://rickandmortyapi.com/api/character/avatar/275.jpeg", character1.image)
    }

    @Test
    fun getOrigin() {
        assertEquals(origin1.name, character1.origin?.name)
    }

}