package ru.ellaid.user.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Indexed(unique=true)
    val login: String,
    val password: String,
    @Id
    val id: String? = null,
)
