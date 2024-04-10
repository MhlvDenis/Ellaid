package ru.ellaid.track.rest.form

data class CreateTrackForm(
    val name: String,
    val author: String,
    val musicUrl: String,
    val coverUrl: String
)
