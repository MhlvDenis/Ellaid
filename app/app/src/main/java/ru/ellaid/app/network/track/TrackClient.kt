package ru.ellaid.app.network.track

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.ellaid.app.common.Constants
import ru.ellaid.app.data.entity.Track
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class TrackClient @Inject constructor(
    private val client: OkHttpClient,
) {

    companion object {
        private const val FETCH_TRACKS_PATH = "/tracks"
        private const val SEARCH_BY_PATTERN_PATH_SEGMENT = "search"
    }

    fun fetchTracks(
        trackIds: List<String>,
        callback: (List<Track>) -> Unit
    ) {
        try {
            val request = Request.Builder()
                .url(Constants.BASE_URL + FETCH_TRACKS_PATH)
                .post(Gson().toJson(trackIds).toRequestBody("application/json".toMediaTypeOrNull()))
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(emptyList())
                }

                override fun onResponse(call: Call, response: Response) {
                    when (response.code) {
                        HttpURLConnection.HTTP_OK -> {
                            val typeToken = object : TypeToken<List<Track>>() {}.type
                            val tracks: List<Track> = Gson().fromJson(response.body!!.string(), typeToken)
                            callback(tracks)
                        }
                        else -> {
                            Log.println(
                                Log.WARN,
                                "TrackClient",
                                "Unknown response from /tracks: $response"
                            )
                            callback(emptyList())
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Log.println(
                Log.ERROR,
                "TrackClient",
                "Error during fetch tracks: $e"
            )
            callback(emptyList())
        }
    }

    fun searchWithTypos(
        pattern: String,
        callback: (List<Track>) -> Unit,
    ) {
        try {
            val url = HttpUrl.Builder()
                .scheme(Constants.SCHEME)
                .host(Constants.HOST)
                .addPathSegment(SEARCH_BY_PATTERN_PATH_SEGMENT)
                .addQueryParameter("pattern", pattern)
                .build()

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(emptyList())
                }

                override fun onResponse(call: Call, response: Response) {
                    when (response.code) {
                        HttpURLConnection.HTTP_OK -> {
                            val typeToken = object : TypeToken<List<Track>>() {}.type
                            val tracks: List<Track> = Gson().fromJson(response.body!!.string(), typeToken)
                            callback(tracks)
                        }
                        else -> {
                            Log.println(
                                Log.WARN,
                                "TrackClient",
                                "Unknown response from /tracks: $response"
                            )
                            callback(emptyList())
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Log.println(
                Log.ERROR,
                "TrackClient",
                "Error during fetch tracks: $e"
            )
            callback(emptyList())
        }
    }
}
