package ru.ellaid.comment.rest.form

data class EditCommentForm(
    val commentId: String,
    val newContent: String,
)