package ru.ellaid.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ellaid.app.network.auth.AuthClient
import ru.ellaid.app.network.auth.status.RegisterStatus
import javax.inject.Inject

data class RegisteredUser(
    val message: String
)

data class RegistrationErrorOccurred(
    val message: String
)

data class RegistrationResult(
    val success: RegisteredUser? = null,
    val error: RegistrationErrorOccurred? = null,
)

data class RegistrationFormState(
    val isDataValid: Boolean = false
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authClient: AuthClient
) : ViewModel() {

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    private val _registrationFormState = MutableLiveData<RegistrationFormState>()
    val registrationFormState: LiveData<RegistrationFormState> = _registrationFormState

    fun register(username: String, password: String) {
        authClient.register(username, password) { result ->
            when (result) {
                RegisterStatus.OK -> {
                    _registrationResult.postValue(
                        RegistrationResult(success = RegisteredUser(message = "$username is successfully registered"))
                    )
                    Log.println(Log.INFO, "login", "$username registration successful")
                }
                RegisterStatus.USER_ALREADY_EXISTS -> {
                    _registrationResult.postValue(
                        RegistrationResult(error = RegistrationErrorOccurred(message = "$username is already registered"))
                    )
                    Log.println(Log.INFO, "login", "$username already exists")
                }
                RegisterStatus.CALL_FAILURE -> {
                    _registrationResult.postValue(
                        RegistrationResult(error = RegistrationErrorOccurred(message = "Something's wrong with network"))
                    )
                    Log.println(Log.INFO, "login", "Something's wrong with network")
                }
                RegisterStatus.UNKNOWN_RESPONSE -> {
                    Log.println(Log.ERROR, "register", "Unknown response")
                }
            }
        }
    }

    fun registrationDataChanged(username: String, password: String, passwordConfirm: String) {
        if (isUsernameValid(username) && isPasswordValid(password, passwordConfirm)) {
            _registrationFormState.value = RegistrationFormState(isDataValid = true)
        } else {
            _registrationFormState.value = RegistrationFormState(isDataValid = false)
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        return username.isNotEmpty()
    }

    private fun isPasswordValid(password: String, passwordConfirm: String): Boolean {
        return password.isNotEmpty() && password == passwordConfirm
    }
}