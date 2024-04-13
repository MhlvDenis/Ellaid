package ru.ellaid.jwt.auth.helper

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import ru.ellaid.jwt.auth.exception.InvalidJwtTokenException
import java.security.SignatureException
import java.util.*

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
        userId: String
    ): String = generateToken(username, mapOf("UserId" to userId))

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

    override fun extractIssuer(token: String): String = extractClaim(token) { it.issuer }

    override fun isTokenValid(username: String, token: String): Boolean =
        (username == extractUsername(token)) && !isTokenExpired(token)

    private fun extractExpiration(token: String): Date = extractClaim(token) { it.expiration }

    private fun isTokenExpired(token: String): Boolean =
        extractExpiration(token).before(Date(System.currentTimeMillis()))

    private fun <T> extractClaim(
        token: String,
        resolver: (Claims) -> T
    ): T = resolver(extractAllClaims(token))

    private fun extractAllClaims(token: String): Claims = try {
        Jwts.parserBuilder()
            .setSigningKey(signKey)
            .build()
            .parseClaimsJwt(token)
            .body
    } catch (e: Exception) {
        when (e) {
            is ExpiredJwtException, is UnsupportedJwtException,
            is MalformedJwtException, is SignatureException,
            is IllegalArgumentException -> throw InvalidJwtTokenException()
            else -> throw e
        }
    }
}
