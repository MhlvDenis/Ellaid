package ru.ellaid.app.network.auth

import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.ellaid.app.network.CredentialsHolder
import ru.ellaid.app.network.auth.error.LoginStatus
import ru.ellaid.app.network.auth.error.RegisterStatus
import ru.ellaid.app.network.auth.form.LoginPasswordForm
import ru.ellaid.app.other.Constants
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthClient @Inject constructor(
    private val client: OkHttpClient,
) {

    companion object {
        private const val SIGN_IN_PATH = "/auth/sign-in"
        private const val SIGN_UP_PATH = "/auth/sign-up"
    }

    fun login(
        username: String,
        password: String,
        callback: (LoginStatus) -> Unit
    ) {
        val request = Request.Builder()
            .url(Constants.BASE_URL + SIGN_IN_PATH)
            .post(Gson().toJson(LoginPasswordForm(username, password))
                .toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(LoginStatus.CALL_FAILURE)
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    HttpURLConnection.HTTP_OK -> {
                        CredentialsHolder.token = response.body!!.string()
                        callback(LoginStatus.OK)
                    }
                    HttpURLConnection.HTTP_UNAUTHORIZED -> callback(LoginStatus.INCORRECT_DATA)
                    else -> callback(LoginStatus.UNKNOWN_RESPONSE)
                }
            }
        })
    }

    fun register(
        username: String,
        password: String,
        callback: (RegisterStatus) -> Unit
    ) {
        val request = Request.Builder()
            .url(Constants.BASE_URL + SIGN_UP_PATH)
            .post(Gson().toJson(LoginPasswordForm(username, password))
                .toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(RegisterStatus.CALL_FAILURE)
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    HttpURLConnection.HTTP_CREATED -> callback(RegisterStatus.OK)
                    HttpURLConnection.HTTP_CONFLICT -> callback(RegisterStatus.USER_ALREADY_EXISTS)
                    else -> callback(RegisterStatus.UNKNOWN_RESPONSE)
                }
            }
        })
    }
}