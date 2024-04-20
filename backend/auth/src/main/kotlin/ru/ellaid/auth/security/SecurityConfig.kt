package ru.ellaid.auth.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
open class SecurityConfig {

    @Bean
    open fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain =
        http.csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/auth/sign-up",
                    "/auth/sign-in",
                    "/auth/validate"
                ).permitAll()
            }
            .build()
}
