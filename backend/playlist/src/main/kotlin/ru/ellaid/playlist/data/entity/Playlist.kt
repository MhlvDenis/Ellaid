package ru.ellaid.playlist.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "playlists")
data class Playlist(
    val title: String,
    @Indexed
    val userId: String,
    val trackIds: MutableList<String> = ArrayList(),
    @Id
    val id: String? = null
)
