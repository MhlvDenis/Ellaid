package ru.ellaid.user.data.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.ellaid.user.data.entity.User

interface UsersRepository: MongoRepository<User, String> {

    fun findUserByLogin(login: String?): User?
}
