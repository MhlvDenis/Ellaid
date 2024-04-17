package ru.ellaid.jwt.auth

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.ellaid.jwt.auth.helper.JwtAuthHelper
import ru.ellaid.jwt.auth.helper.JwtAuthHelperImpl

class JwtAuthTest {
    companion object {
        private const val JWT_SECRET_KEY = "salfjashgiohsondlfnsdiajfsdjfiosdjfpjadfeawfead"
        private const val ISSUER = "Ellaid"
        private const val EXPIRATION_TIME_MS = 100_000_000L
    }

    @Test
    fun generate_validate() {
        val jwtAuthHelper = JwtAuthHelperImpl(JWT_SECRET_KEY, ISSUER, EXPIRATION_TIME_MS)

        val username = "username"
        val userId = "ahahhahahaha"
        val role = "ROLE_USER"
        val token = jwtAuthHelper.generateToken(username, userId, role)
        validateToken(token, jwtAuthHelper, username, userId, role)
    }

    private fun validateToken(
        token: String,
        jwtAuthHelper: JwtAuthHelper,
        username: String,
        userId: String,
        userRole: String
    ) {
        val claimUsername = jwtAuthHelper.extractUsername(token)
        val claimUserId = jwtAuthHelper.extractUserId(token)
        val claimRole = jwtAuthHelper.extractRole(token)
        val claimIssuer = jwtAuthHelper.extractIssuer(token)
        val isTokenExpired = jwtAuthHelper.isTokenExpired(token)

        Assertions.assertEquals(username, claimUsername)
        Assertions.assertEquals(userId, claimUserId)
        Assertions.assertEquals(userRole, claimRole)
        Assertions.assertEquals(ISSUER, claimIssuer)
        Assertions.assertFalse(isTokenExpired)
    }
}