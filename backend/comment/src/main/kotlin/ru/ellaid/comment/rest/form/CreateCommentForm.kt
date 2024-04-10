package ru.ellaid.comment.rest.form

data class CreateCommentForm(
    val trackId: String,
    val userId: String,
    val content: String,
)