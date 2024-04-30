package ru.ellaid.app.network.track

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.ellaid.app.data.entity.Track
import ru.ellaid.app.network.CredentialsHolder
import ru.ellaid.app.common.Constants
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class TrackClient @Inject constructor(
    private val client: OkHttpClient,
) {

    suspend fun getAllSongs(): List<Track> = try {
        val request = Request
            .Builder()
            .url(Constants.BASE_URL + "track/getAll")
            .header("Authorization", CredentialsHolder.token!!)
            .get()
            .build()
        var result = emptyList<Track>()
        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    (HttpURLConnection.HTTP_UNAUTHORIZED) -> {
                        result = emptyList()
                        Log.println(Log.WARN, "Unauthorized", "You are suddenly unauthorized")
                    }

                    (HttpURLConnection.HTTP_INTERNAL_ERROR) -> {
                        result = emptyList()
                        Log.println(Log.ERROR, "Server Error", "Server couldn't find tracks")
                    }

                    (HttpURLConnection.HTTP_OK) -> {
                        val typeToken = object : TypeToken<List<Track>>() {}.type
                        result = Gson().fromJson(response.body!!.string(), typeToken)
                        Log.println(Log.INFO, "Got tracks", result.size.toString())
                        for (song in result) {
                            Log.println(Log.INFO, "track", Gson().toJson(song))
                        }
                    }
                }
                countDownLatch.countDown()
            }
        })
        withContext(Dispatchers.IO) {
            countDownLatch.await()
        }

        result
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun searchWithTypos(input: String, typos: Int): List<Track> = try {
        val url = HttpUrl.Builder()
            .scheme(Constants.SCHEME)
            .host(Constants.HOST)
            .port(Constants.PORT)
            .addPathSegment("elysium")
            .addPathSegment("track")
            .addPathSegments("searchWithTypos")
            .addQueryParameter("input", input)
            .addQueryParameter("typos", typos.toString())
            .build()
        val request = Request
            .Builder()
            .url(url)
            .header("Authorization", CredentialsHolder.token!!)
            .get()
            .build()
        var result = emptyList<Track>()
        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    (HttpURLConnection.HTTP_UNAUTHORIZED) -> {
                        result = emptyList()
                        Log.println(Log.WARN, "Unauthorized", "You are suddenly unauthorized")
                    }

                    (HttpURLConnection.HTTP_NO_CONTENT) -> {
                        result = emptyList()
                        Log.println(
                            Log.WARN,
                            "No content",
                            "No songs found"
                        )
                    }

                    (HttpURLConnection.HTTP_OK) -> {
                        val typeToken = object : TypeToken<List<Track>>() {}.type
                        result = Gson().fromJson(response.body!!.string(), typeToken)
                        Log.println(Log.INFO, "Got songs", result.size.toString())
                        for (song in result) {
                            Log.println(Log.INFO, "song", Gson().toJson(song))
                        }
                    }
                }
                countDownLatch.countDown()
            }
        })
        withContext(Dispatchers.IO) {
            countDownLatch.await()
        }

        result
    } catch (e: Exception) {
        emptyList()
    }
}
