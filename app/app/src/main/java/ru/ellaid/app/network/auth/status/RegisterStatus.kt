package ru.ellaid.app.network.auth.status

enum class RegisterStatus {
    OK,
    CALL_FAILURE,
    UNKNOWN_RESPONSE,
    USER_ALREADY_EXISTS
}
