package ru.ellaid.auth.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.ellaid.auth.data.entity.User
import ru.ellaid.auth.exception.AuthenticationFailedException
import ru.ellaid.auth.exception.UserNotFoundException
import ru.ellaid.jwt.auth.JwtAuthConfig
import ru.ellaid.jwt.auth.exception.InvalidJwtTokenException
import ru.ellaid.jwt.auth.helper.JwtAuthHelper

private val logger = KotlinLogging.logger { }

@Service
@Import(
    value = [
        JwtAuthConfig::class,
    ]
)
class AuthService(
    private val userService: UserService,
    private val jwtAuthHelper: JwtAuthHelper,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${app.security.issuer}")
    private val issuer: String,
) {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

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
            val user = userService.getUserByLogin(login)
            jwtAuthHelper.generateToken(
                user.login,
                user.id!!,
                user.role.name
            )
        } else {
            throw AuthenticationFailedException()
        }
    }

    fun isTokenValid(
        rawToken: String
    ): Boolean = try {
        val token = if (rawToken.startsWith(BEARER_PREFIX)) {
            rawToken.substring(BEARER_PREFIX.length)
        } else {
            throw InvalidJwtTokenException()
        }

        val claimUsername = jwtAuthHelper.extractUsername(token)
        val claimUserId = jwtAuthHelper.extractUserId(token)
        val claimRole = jwtAuthHelper.extractRole(token)
        val claimIssuer = jwtAuthHelper.extractIssuer(token)
        val isTokenExpired = jwtAuthHelper.isTokenExpired(token)

        val actualUser = userService.getUserByLogin(claimUsername)
        (actualUser.login == claimUsername) &&
                (actualUser.id == claimUserId) &&
                (actualUser.role.name == claimRole) &&
                (issuer == claimIssuer) &&
                (!isTokenExpired)
    } catch (e: Exception) {
        when (e) {
            is UserNotFoundException, is InvalidJwtTokenException -> {
                logger.error { "Got invalid jwt token: $rawToken" }
                false
            }
            else -> throw e
        }
    }
}
