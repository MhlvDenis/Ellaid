package ru.ellaid.track.rest.dto

data class TrackDto(
    val name: String,
    val author: String,
    val musicUrl: String,
    val coverUrl: String
)
