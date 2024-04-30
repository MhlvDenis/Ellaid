package ru.ellaid.app.network.comment

import android.util.Log
import com.google.common.net.HttpHeaders
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
import ru.ellaid.app.data.entity.Comment
import ru.ellaid.app.network.CredentialsHolder
import ru.ellaid.app.network.comment.cache.CommentCache
import ru.ellaid.app.network.comment.form.AddCommentForm
import ru.ellaid.app.network.comment.status.AddCommentStatus
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class CommentClient @Inject constructor(
    private val client: OkHttpClient,
) {

    companion object {
        private const val ADD_COMMENT_PATH = "/comment"
        private const val FETCH_COMMENTS_PATH = "/comments"
    }

    private val commentCache: CommentCache = CommentCache()

    fun fetchComments(
        trackId: String,
        callback: (List<Comment>) -> Unit,
    ) {
        val cached = commentCache.loadFromCache(trackId)
        if (cached != null) {
            callback(cached)
            return
        }

        try {
            val url = HttpUrl.Builder()
                .scheme(Constants.SCHEME)
                .host(Constants.HOST)
                .addPathSegment(FETCH_COMMENTS_PATH)
                .addQueryParameter("trackId", trackId)
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
                            val typeToken = object : TypeToken<List<Comment>>() {}.type
                            val comments: List<Comment> = Gson().fromJson(response.body!!.string(), typeToken)
                            commentCache.addToCache(trackId, comments)
                            callback(comments)
                        }
                        else -> {
                            Log.println(
                                Log.ERROR, "CommentClient",
                                "Unknown response from /comments: $response"
                            )
                            callback(emptyList())
                        }
                    }
                }
            })
        } catch (e: Exception) {
            Log.println(
                Log.ERROR,
                "CommentClient",
                "Error during fetch comments for track $trackId: $e"
            )
            callback(emptyList())
        }
    }

    fun addComment(
        trackId: String,
        content: String,
        callback: (AddCommentStatus) -> Unit
    ) {
        val request = Request.Builder()
            .url(Constants.BASE_URL + ADD_COMMENT_PATH)
            .post(Gson().toJson(AddCommentForm(trackId, content))
                .toRequestBody("application/json".toMediaTypeOrNull()))
            .addHeader(HttpHeaders.AUTHORIZATION, CredentialsHolder.token!!)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(AddCommentStatus.CALL_FAILURE)
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    HttpURLConnection.HTTP_OK -> {
                        val typeToken = object : TypeToken<Comment>() {}.type
                        val newComment: Comment = Gson().fromJson(response.body!!.string(), typeToken)
                        commentCache.updateCache(trackId, newComment)
                        callback(AddCommentStatus.OK)
                    }
                    HttpURLConnection.HTTP_FORBIDDEN -> callback(AddCommentStatus.UNAUTHORIZED)
                    else -> callback(AddCommentStatus.UNKNOWN_RESPONSE)
                }
            }
        })
    }
}
