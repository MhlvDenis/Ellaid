package ru.ellaid.app.network.auth.error

enum class RegisterStatus {
    OK,
    CALL_FAILURE,
    UNKNOWN_RESPONSE,
    USER_ALREADY_EXISTS
}
