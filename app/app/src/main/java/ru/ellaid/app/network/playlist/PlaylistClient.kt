package ru.ellaid.app.network.playlist

import android.util.Log
import com.google.common.net.HttpHeaders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.ellaid.app.common.Constants
import ru.ellaid.app.network.CredentialsHolder
import ru.ellaid.app.network.playlist.form.CreatePlaylistForm
import ru.ellaid.app.network.playlist.form.HandleTrackForm
import ru.ellaid.app.network.playlist.form.PlaylistDto
import ru.ellaid.app.network.playlist.status.AddTrackStatus
import ru.ellaid.app.network.playlist.status.CreatePlaylistStatus
import ru.ellaid.app.network.playlist.status.RemoveTrackStatus
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class PlaylistClient @Inject constructor(
    private val client: OkHttpClient,
) {

    companion object {
        private const val FETCH_PLAYLISTS_PATH = "/playlists"
        private const val CREATE_PLAYLIST_PATH = "/playlist"
        private const val ADD_TRACK_TO_PLAYLIST_PATH = "/playlist/add-track"
        private const val REMOVE_TRACK_FROM_PLAYLIST_PATH = "/playlist/remove-track"
    }

    fun fetchPlaylists(
        callback: (List<PlaylistDto>) -> Unit,
    ) {
        try {
            val request = Request.Builder()
                .url(Constants.BASE_URL + FETCH_PLAYLISTS_PATH)
                .get()
                .addHeader(HttpHeaders.AUTHORIZATION, CredentialsHolder.token!!)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(emptyList())
                }

                override fun onResponse(call: Call, response: Response) {
                    when (response.code) {
                        HttpURLConnection.HTTP_OK -> {
                            val typeToken = object : TypeToken<List<PlaylistDto>>() {}.type
                            val playlistsDto: List<PlaylistDto> = Gson().fromJson(response.body!!.string(), typeToken)
                            callback(playlistsDto)
                        }
                        else -> {
                            Log.println(
                                Log.WARN,
                                "PlaylistClient",
                                "Unknown response from /playlists: $response"
                            )
                            callback(emptyList())
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Log.println(
                Log.ERROR,
                "PlaylistClient",
                "Error during fetch tracks: $e"
            )
            callback(emptyList())
        }
    }

    fun createPlaylist(
        title: String,
        callback: (CreatePlaylistStatus, PlaylistDto?) -> Unit,
    ) {
        val request = Request.Builder()
            .url(Constants.BASE_URL + CREATE_PLAYLIST_PATH)
            .post(Gson().toJson(CreatePlaylistForm(title)).toRequestBody("application/json".toMediaTypeOrNull()))
            .addHeader(HttpHeaders.AUTHORIZATION, CredentialsHolder.token!!)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(CreatePlaylistStatus.CALL_FAILURE, null)
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    HttpURLConnection.HTTP_CREATED -> {
                        val typeToken = object : TypeToken<PlaylistDto>() {}.type
                        val playlistDto: PlaylistDto = Gson().fromJson(response.body!!.string(), typeToken)
                        callback(CreatePlaylistStatus.OK, playlistDto)
                    }
                    HttpURLConnection.HTTP_FORBIDDEN -> callback(CreatePlaylistStatus.UNAUTHORIZED, null)
                    HttpURLConnection.HTTP_CONFLICT -> callback(CreatePlaylistStatus.PLAYLIST_ALREADY_EXISTS, null)
                    else -> callback(CreatePlaylistStatus.UNKNOWN_RESPONSE, null)
                }
            }
        })
    }

    fun addTrackToPlaylist(
        playlistId: String,
        trackId: String,
        callback: (AddTrackStatus, PlaylistDto?) -> Unit,
    ) {
        val request = Request.Builder()
            .url(Constants.BASE_URL + ADD_TRACK_TO_PLAYLIST_PATH)
            .patch(Gson().toJson(HandleTrackForm(playlistId, trackId))
                .toRequestBody("application/json".toMediaTypeOrNull()))
            .addHeader(HttpHeaders.AUTHORIZATION, CredentialsHolder.token!!)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(AddTrackStatus.CALL_FAILURE, null)
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    HttpURLConnection.HTTP_CREATED -> {
                        val typeToken = object : TypeToken<PlaylistDto>() {}.type
                        val playlistDto: PlaylistDto = Gson().fromJson(response.body!!.string(), typeToken)
                        callback(AddTrackStatus.OK, playlistDto)
                    }
                    HttpURLConnection.HTTP_FORBIDDEN -> callback(AddTrackStatus.UNAUTHORIZED, null)
                    HttpURLConnection.HTTP_CONFLICT -> callback(AddTrackStatus.TRACK_ALREADY_ADDED, null)
                    else -> callback(AddTrackStatus.UNKNOWN_RESPONSE, null)
                }
            }
        })
    }

    fun removeTrackFromPlaylist(
        playlistId: String,
        trackId: String,
        callback: (RemoveTrackStatus, PlaylistDto?) -> Unit,
    ) {
        val request = Request.Builder()
            .url(Constants.BASE_URL + REMOVE_TRACK_FROM_PLAYLIST_PATH)
            .patch(Gson().toJson(HandleTrackForm(playlistId, trackId))
                .toRequestBody("application/json".toMediaTypeOrNull()))
            .addHeader(HttpHeaders.AUTHORIZATION, CredentialsHolder.token!!)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(RemoveTrackStatus.CALL_FAILURE, null)
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    HttpURLConnection.HTTP_CREATED -> {
                        val typeToken = object : TypeToken<PlaylistDto>() {}.type
                        val playlistDto: PlaylistDto = Gson().fromJson(response.body!!.string(), typeToken)
                        callback(RemoveTrackStatus.OK, playlistDto)
                    }
                    HttpURLConnection.HTTP_FORBIDDEN -> callback(RemoveTrackStatus.UNAUTHORIZED, null)
                    HttpURLConnection.HTTP_NOT_FOUND -> callback(RemoveTrackStatus.NOT_FOUND, null)
                    else -> callback(RemoveTrackStatus.UNKNOWN_RESPONSE, null)
                }
            }
        })
    }
}
