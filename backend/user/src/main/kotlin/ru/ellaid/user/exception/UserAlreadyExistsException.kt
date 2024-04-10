package ru.ellaid.user.exception

class UserAlreadyExistsException(
    message: String?
): RuntimeException(message)