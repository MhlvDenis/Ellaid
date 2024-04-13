package ru.ellaid.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class AuthConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain =
        http.csrf { it.disable() }
            .authorizeHttpRequests { it.requestMatchers("/auth/sign-up", "/auth/sign-in").permitAll() }
            .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
