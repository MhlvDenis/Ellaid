package ru.ellaid.user.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ellaid.user.form.UserCredentialsForm
import ru.ellaid.user.service.AuthService
import ru.ellaid.user.service.Status

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(
        @RequestBody userCredentialsForm: UserCredentialsForm
    ): ResponseEntity<String> {
        val result = authService.login(userCredentialsForm.login, userCredentialsForm.password)

        return when (result.status) {
            Status.SUCCESS -> ResponseEntity(result.data, HttpStatus.OK)
            else -> ResponseEntity(result.message, HttpStatus.FORBIDDEN)
        }
    }

    @PostMapping("/register")
    fun register(
        @RequestBody userCredentialsForm: UserCredentialsForm
    ): ResponseEntity<String> {
        val result = authService.register(userCredentialsForm.login, userCredentialsForm.password)

        return when (result.status) {
            Status.SUCCESS -> ResponseEntity("User created", HttpStatus.CREATED)
            else -> ResponseEntity(result.message, HttpStatus.BAD_REQUEST)
        }
    }
}
