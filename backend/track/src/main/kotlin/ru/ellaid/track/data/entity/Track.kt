package ru.ellaid.track.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "tracks")
data class Track(
    val name: String,
    val author: String,
    @Indexed(unique = true)
    val musicUrl: String,
    val coverUrl: String,
    @Id
    val id: String? = null
)
