package ru.ellaid.app.network.auth.error

enum class RegisterError {
    OK,
    CALL_FAILURE,
    UNKNOWN_RESPONSE,
    USER_ALREADY_EXISTS
}
