package ru.ellaid.comment.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher

@Configuration
@EnableWebSecurity
open class SecurityConfig {

    companion object {
        private val URLS = OrRequestMatcher(AntPathRequestMatcher("/comment/**"))
    }

    @Bean
    open fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain =
        http.csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(URLS).permitAll()
                it.requestMatchers("/comments").permitAll()
            }
            .build()
}
