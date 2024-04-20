package ru.ellaid.jwt.auth.helper

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import ru.ellaid.jwt.auth.exception.InvalidJwtTokenException
import java.util.*

private val logger = KotlinLogging.logger { }

class JwtAuthHelperImpl(
    jwtSecretKey: String,
    private val issuer: String,
    private val expirationTime: Long,
): JwtAuthHelper {

    private val signKey = Keys.hmacShaKeyFor(
        Decoders.BASE64.decode(jwtSecretKey)
    )

    override fun generateToken(
        username: String,
        userId: String,
        role: String
    ): String = generateToken(username, mapOf("UserId" to userId, "Role" to role))

    override fun generateToken(
        username: String,
        claims: Map<String, Any>
    ): String =
        Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuer(issuer)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(signKey, SignatureAlgorithm.HS256)
            .compact()

    override fun extractUsername(token: String): String = extractClaim(token) { it.subject }

    override fun extractUserId(token: String): String = extractClaim(token) { claims ->
        claims.get("UserId", String::class.java)
    }

    override fun extractRole(token: String): String = extractClaim(token) { claims ->
        claims.get("Role", String::class.java)
    }

    override fun extractIssuer(token: String): String = extractClaim(token) { it.issuer }

    override fun isTokenExpired(token: String): Boolean =
        extractExpiration(token).before(Date(System.currentTimeMillis()))

    private fun extractExpiration(token: String): Date = extractClaim(token) { it.expiration }

    private fun <T> extractClaim(
        token: String,
        resolver: (Claims) -> T
    ): T = resolver(extractAllClaims(token))

    private fun extractAllClaims(token: String): Claims = try {
        Jwts.parserBuilder()
            .setSigningKey(signKey)
            .build()
            .parseClaimsJws(token)
            .body
    } catch (e: Exception) {
        logger.error { e.message }
        when (e) {
            is ExpiredJwtException, is UnsupportedJwtException,
            is MalformedJwtException, is SecurityException,
            is IllegalArgumentException -> throw InvalidJwtTokenException()
            else -> throw e
        }
    }
}
