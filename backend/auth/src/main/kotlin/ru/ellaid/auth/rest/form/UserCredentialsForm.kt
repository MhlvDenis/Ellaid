package ru.ellaid.auth.rest.form

data class UserCredentialsForm(
    val login: String,
    val password: String,
)

