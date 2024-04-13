package ru.ellaid.auth.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.ellaid.auth.data.entity.User
import ru.ellaid.auth.data.repository.UserRepository
import ru.ellaid.auth.exception.DuplicateUserLoginException
import ru.ellaid.auth.exception.UserNotFoundException

@Service
class UserService(
    private val repository: UserRepository
): UserDetailsService {

    fun createUser(
        login: String,
        password: String,
    ): User =
        if (repository.findUserByLogin(login) != null) {
            throw DuplicateUserLoginException()
        } else {
            repository.save(User(login, password))
        }

    override fun loadUserByUsername(username: String?): UserDetails =
        if (username != null) {
            repository.findUserByLogin(username) ?: throw UserNotFoundException()
        } else {
            throw UserNotFoundException()
        }
}
