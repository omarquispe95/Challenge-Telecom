package com.example.challengetelecom.data.model

import java.io.Serializable

data class Character (
    var id: Int,
    var name: String,
    var status: String,
    var species: String,
    var type: String,
    var gender: String,
    var origin: Info,
    var location: Info,
    var image: String,
    var episode: List<String>,
    var url: String,
    var created: String
) : Serializable