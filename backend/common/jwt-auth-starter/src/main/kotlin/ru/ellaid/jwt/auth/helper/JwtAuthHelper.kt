package ru.ellaid.jwt.auth.helper

interface JwtAuthHelper {

    fun generateToken(username: String, userId: String, role: String): String

    fun generateToken(username: String, claims: Map<String, Any>): String

    fun extractUsername(token: String): String

    fun extractUserId(token: String): String

    fun extractRole(token: String): String

    fun extractIssuer(token: String): String

    fun isTokenExpired(token: String): Boolean
}
