package ru.ellaid.auth.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.ellaid.auth.data.entity.User

@Service
class AuthService(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
) {

    fun signUp(
        login: String,
        rawPassword: String
    ): User = userService.createUser(login, passwordEncoder.encode(rawPassword))
}
