package ru.ellaid.app.network.playlist.form

data class PlaylistDto(
    val id: String,
    val title: String,
    val trackIds: List<String>,
)
