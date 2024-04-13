package ru.ellaid.jwt.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.ellaid.jwt.auth.helper.JwtAuthHelper
import ru.ellaid.jwt.auth.helper.JwtAuthHelperImpl

@Configuration
@ConditionalOnProperty(
    prefix = "app.security",
    name = ["jwt-secret-key", "issuer", "expiration-time-ms"]
)
open class JwtAuthConfig {

    @Bean
    open fun jwtAuthHelper(
        @Value("\${app.security.jwt-secret-key}")
        jwtSecretKey: String,
        @Value("\${app.security.issuer}")
        issuer: String,
        @Value("\${app.security.expiration-time-ms}")
        expirationTime: Long
    ): JwtAuthHelper = JwtAuthHelperImpl(jwtSecretKey, issuer, expirationTime)
}
