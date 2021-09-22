package com.example.challengetelecom.data.model

import java.io.Serializable

data class Character (
    var id: Int,
    var name: String,
    var status: String, // TODO change
    var type: String,
    var gender: String, // TODO change
    var origin: Info,
    var location: Info,
    var image: String,
    var episode: List<String>,
    var url: String,
    var created: String
) : Serializable