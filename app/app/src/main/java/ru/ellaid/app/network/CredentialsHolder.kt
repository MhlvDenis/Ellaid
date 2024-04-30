package ru.ellaid.app.network

import android.util.Log

object CredentialsHolder {

    private const val ACCESS_TOKEN_PREFIX = "Bearer "

    var token: String? = null
        get() = ACCESS_TOKEN_PREFIX + field
        set(value) {
            field = value
            Log.d("CredentialsHolder", "Update access token")
        }
}
