package ru.ellaid.auth.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import ru.ellaid.auth.data.entity.Role
import ru.ellaid.auth.data.entity.User
import ru.ellaid.auth.exception.DuplicateUserLoginException
import ru.ellaid.auth.service.UserService

@Configuration
open class AuthConfig {

    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    open fun authenticationProvider(
        userService: UserService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider =
        DaoAuthenticationProvider().apply {
            setUserDetailsService(userService)
            setPasswordEncoder(passwordEncoder)
        }

    @Bean
    open fun authenticationManager(
        config: AuthenticationConfiguration
    ): AuthenticationManager = config.authenticationManager

    @Bean
    @ConditionalOnProperty(
        prefix = "app.security",
        name = ["admin-username", "admin-password"]
    )
    open fun admin(
        @Value("\${app.security.admin-username}")
        username: String,
        @Value("\${app.security.admin-password}")
        password: String,
        userService: UserService,
        passwordEncoder: PasswordEncoder
    ): User = try {
        userService.createUser(username, passwordEncoder.encode(password), Role.ROLE_ADMIN)
    } catch (e: DuplicateUserLoginException) {
        userService.getUserByLogin(username)
    }
}
