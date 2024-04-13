package ru.ellaid.auth.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ellaid.auth.exception.AuthenticationFailedException
import ru.ellaid.auth.exception.DuplicateUserLoginException
import ru.ellaid.auth.rest.form.UserCredentialsForm
import ru.ellaid.auth.service.AuthService

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/sign-up")
    fun signUp(
        @RequestBody userCredentialsForm: UserCredentialsForm
    ): ResponseEntity<String> = try {
        ResponseEntity(
            authService.signUp(
                userCredentialsForm.login,
                userCredentialsForm.password
            ).login,
            HttpStatus.CREATED
        )
    } catch (e: DuplicateUserLoginException) {
        ResponseEntity(HttpStatus.CONFLICT)
    }

    @PostMapping("/sign-in")
    fun signIn(
        @RequestBody userCredentialsForm: UserCredentialsForm
    ): ResponseEntity<String> = try {
        ResponseEntity(
            authService.signIn(
                userCredentialsForm.login,
                userCredentialsForm.password
            ),
            HttpStatus.OK
        )
    } catch (e: AuthenticationFailedException) {
        ResponseEntity(HttpStatus.FORBIDDEN)
    }
}
