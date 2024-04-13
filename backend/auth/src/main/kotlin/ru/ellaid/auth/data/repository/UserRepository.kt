package ru.ellaid.auth.data.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.ellaid.auth.data.entity.User

interface UserRepository: MongoRepository<User, String> {

    fun findUserByLogin(login: String): User?
}
