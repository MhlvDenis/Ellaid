package ru.ellaid.comment.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.ellaid.jwt.auth.JwtAuthConfig
import ru.ellaid.jwt.auth.filter.JwtAuthFilter

@Configuration
@EnableWebSecurity
@Import(value = [
    JwtAuthConfig::class
])
open class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {

    companion object {
        private const val USER = "USER"
    }

    @Bean
    open fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain =
        http.csrf { it.disable() }
            .authorizeHttpRequests { request -> request
                .requestMatchers(HttpMethod.POST, "/comment").hasRole(USER)
                .requestMatchers(HttpMethod.PATCH, "/comment").hasRole(USER)
                .requestMatchers(HttpMethod.DELETE, "/comment").hasRole(USER)
                .requestMatchers(HttpMethod.GET, "/comments").permitAll()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}
