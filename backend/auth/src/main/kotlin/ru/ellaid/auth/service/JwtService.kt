package ru.ellaid.auth.service

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.SignatureException
import java.util.*

@Service
class JwtService(
    @Value("\${app.security.jwt-secret-key}")
    private val jwtSecretKey: String
) {

    fun validateToken(
        token: String
    ): TokenState = try {
        Jwts.parserBuilder().build()
            .parseClaimsJwt(token)
        TokenState.VALID
    } catch (e: Exception) {
        when (e) {
            is ExpiredJwtException -> TokenState.EXPIRED
            is UnsupportedJwtException -> TokenState.UNSUPPORTED
            is MalformedJwtException -> TokenState.MALFORMED
            is SignatureException -> TokenState.INVALID_SIGNATURE
            is IllegalArgumentException -> TokenState.INVALID_CLAIM
            else -> throw e
        }
    }

    fun generateToken(
        username: String,
        claims: Map<String, Any>
    ): String =
        Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact()
}

enum class TokenState {
    VALID,
    EXPIRED,
    UNSUPPORTED,
    MALFORMED,
    INVALID_SIGNATURE,
    INVALID_CLAIM,
}
