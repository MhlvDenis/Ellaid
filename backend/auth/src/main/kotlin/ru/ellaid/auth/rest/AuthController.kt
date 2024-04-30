package ru.ellaid.auth.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ellaid.auth.exception.AuthenticationFailedException
import ru.ellaid.auth.exception.DuplicateUserLoginException
import ru.ellaid.auth.rest.form.UserCredentialsForm
import ru.ellaid.auth.rest.form.ValidationResponse
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
        ResponseEntity(HttpStatus.UNAUTHORIZED)
    }

    @GetMapping("/validate")
    fun validate(
        @RequestParam("token") token: String
    ): ResponseEntity<ValidationResponse> =
        ResponseEntity(
            ValidationResponse(authService.isTokenValid(token)),
            HttpStatus.OK
        )
}
