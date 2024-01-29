package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Word(val id: Int, val english: String, val german: String)

data class Dictionary(val words: MutableList<Word>)

val dictionary = mutableListOf<Word>()

val emptyWord = Word(0, "", "")
