package ru.ellaid.track.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "tracks")
data class Track(
    @Id
    val id: String = ObjectId().toString(),
    val name: String,
    val author: String,
    @Indexed(unique = true)
    val musicUrl: String,
    val coverUrl: String,
)
