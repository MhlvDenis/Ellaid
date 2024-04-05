package ru.ellaid.user.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    @Id
    val id: ObjectId = ObjectId(),
    @Indexed(unique=true)
    val login: String,
    val password: String
)
