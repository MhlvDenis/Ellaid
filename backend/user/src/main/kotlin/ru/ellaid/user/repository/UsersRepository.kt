package ru.ellaid.user.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.ellaid.user.entity.User

interface UsersRepository: MongoRepository<User, String> {

    fun findUserByLogin(login: String?): User?
}