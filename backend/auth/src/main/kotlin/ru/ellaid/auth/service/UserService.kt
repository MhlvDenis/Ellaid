package ru.ellaid.auth.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ellaid.auth.data.entity.Role
import ru.ellaid.auth.data.entity.User
import ru.ellaid.auth.data.repository.UserRepository
import ru.ellaid.auth.exception.DuplicateUserLoginException
import ru.ellaid.auth.exception.UserNotFoundException

@Service
@Transactional
open class UserService(
    private val repository: UserRepository
): UserDetailsService {

    fun createUser(
        login: String,
        password: String,
        role: Role = Role.USER
    ): User =
        if (repository.findUserByLogin(login) != null) {
            throw DuplicateUserLoginException()
        } else {
            repository.save(User(login, password, role))
        }

    fun getUserByLogin(login: String): User =
        repository.findUserByLogin(login) ?: throw UserNotFoundException()

    override fun loadUserByUsername(username: String?): UserDetails =
        if (username != null) {
            getUserByLogin(username)
        } else {
            throw UserNotFoundException()
        }
}
