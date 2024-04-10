package ru.ellaid.user.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import ru.ellaid.user.exception.UserAlreadyExistsException

private val logger = KotlinLogging.logger { }

@Service
class AuthService(
    private val userService: UserService,
) {

    fun login(
        login: String?,
        password: String?
    ): Result<String> {
        val user = userService.getUserByLogin(login)
        if (user == null) {
            logger.info { "User $login doesn't exist" }
            return Result(Status.NOT_EXISTS, message = "User $login doesn't exist")
        }

        if (user.password != password) {
            logger.info { "User $login wrong password" }
            return Result(Status.WRONG_CREDENTIALS, message = "Wrong login or password")
        }

        return Result(Status.SUCCESS, data = user.id)
    }

    fun register(
        login: String?,
        password: String?
    ): Result<String> {
        if (!validate(login, password)) {
            logger.info { "User $login empty login or password" }
            return Result(Status.WRONG_DATA_FORMAT, message = "Wrong data format")
        }

        return try {
            userService.addNewUser(login!!, password!!)
            logger.info { "User $login created" }
            Result(Status.SUCCESS)
        } catch (e: UserAlreadyExistsException) {
            logger.info { "User $login already exists" }
            Result(Status.ALREADY_EXISTS, message = "User $login already exists")
        }
    }

    private fun validate(login: String?, password: String?): Boolean
        = !login.isNullOrEmpty() && !password.isNullOrEmpty()
}

data class Result<out T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
)

enum class Status {
    SUCCESS,
    ALREADY_EXISTS,
    NOT_EXISTS,
    WRONG_DATA_FORMAT,
    WRONG_CREDENTIALS,
}
