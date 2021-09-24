package com.example.challengetelecom.data.network.response

import com.example.challengetelecom.data.model.Character

data class CharacterResponse (val info: InfoResponse, val results: List<Character>)