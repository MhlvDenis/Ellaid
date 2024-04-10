package ru.ellaid.comment.data.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "comments")
data class Comment(
    @Indexed
    val trackId: String,
    val userId: String,
    var content: String,
    val time: Instant = Instant.now(),
    @Id
    val id: String? = null,
)
