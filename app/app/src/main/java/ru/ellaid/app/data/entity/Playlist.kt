package ru.ellaid.app.data.entity

data class Playlist(
    val id: String,
    val title: String,
    val tracks: List<Track>,
)
