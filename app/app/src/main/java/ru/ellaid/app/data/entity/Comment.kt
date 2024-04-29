package ru.ellaid.app.data.entity

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.time.Instant

data class Comment(
    val id: String,
    val trackId: String,
    val username: String,
    val content: String,

    @SerializedName("time")
    val publicationDatetime: Instant,
)
