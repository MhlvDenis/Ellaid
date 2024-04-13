package ru.ellaid.jwt.auth.helper

interface JwtAuthHelper {

    fun generateToken(username: String, userId: String): String

    fun generateToken(username: String, claims: Map<String, Any>): String

    fun extractUsername(token: String): String

    fun extractUserId(token: String): String

    fun extractIssuer(token: String): String

    fun isTokenValid(username: String, token: String): Boolean
}