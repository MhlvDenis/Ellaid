package ru.ellaid.auth.service

import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.ellaid.auth.data.entity.User
import ru.ellaid.auth.exception.AuthenticationFailedException
import ru.ellaid.jwt.auth.JwtAuthConfig
import ru.ellaid.jwt.auth.helper.JwtAuthHelper

@Service
@Import(value = [
    JwtAuthConfig::class,
])
class AuthService(
    private val userService: UserService,
    private val jwtAuthHelper: JwtAuthHelper,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
) {

    fun signUp(
        login: String,
        rawPassword: String
    ): User = userService.createUser(login, passwordEncoder.encode(rawPassword))

    fun signIn(
        login: String,
        rawPassword: String
    ): String {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(login, rawPassword)
        )

        return if (auth.isAuthenticated) {
            jwtAuthHelper.generateToken(login, emptyMap())
        } else {
            throw AuthenticationFailedException()
        }
    }
}
