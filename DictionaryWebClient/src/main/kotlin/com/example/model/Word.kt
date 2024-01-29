package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Word(val id: Int, val english: String, val german: String)

val emptyWord = Word(0, "", "")
