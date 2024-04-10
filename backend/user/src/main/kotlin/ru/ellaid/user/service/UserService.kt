package ru.ellaid.user.service

import org.springframework.stereotype.Service
import ru.ellaid.user.data.entity.User
import ru.ellaid.user.exception.UserAlreadyExistsException
import ru.ellaid.user.data.repository.UsersRepository

@Service
class UserService(
    private val usersRepository: UsersRepository
) {

    fun getUserByLogin(login: String?): User? = usersRepository.findUserByLogin(login)

    fun addNewUser(login: String, password: String): User {
        if (usersRepository.findUserByLogin(login) != null) {
            throw UserAlreadyExistsException("User $login already exists")
        }

        return usersRepository.save(
            User(login = login, password = password)
        )
    }
}
