package com.example.challengetelecom.data.network.response

import com.example.challengetelecom.data.model.Character

data class CharacterResponse (var info: InfoResponse, var results: List<Character>)