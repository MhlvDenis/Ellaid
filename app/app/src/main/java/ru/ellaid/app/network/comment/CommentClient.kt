package ru.ellaid.app.network.comment

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.ellaid.app.data.entity.Comment
import ru.ellaid.app.network.CredentialsHolder
import ru.ellaid.app.network.comment.cache.CommentCache
import ru.ellaid.app.network.comment.error.AddCommentError
import ru.ellaid.app.network.comment.form.AddCommentForm
import ru.ellaid.app.other.Constants
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class CommentClient @Inject constructor(
    private val client: OkHttpClient,
) {

    private val commentCache: CommentCache = CommentCache()

    suspend fun loadAllComments(
        trackId: String
    ): List<Comment> = commentCache.loadFromCache(trackId) ?: try {
        val url = HttpUrl.Builder()
            .scheme(Constants.SCHEME)
            .host(Constants.HOST)
            .port(Constants.PORT)
            .addPathSegment("elysium")
            .addPathSegment("comment")
            .addPathSegments("loadAllComments")
            .addQueryParameter("trackId", trackId)
            .build()
        val request = Request
            .Builder()
            .url(url)
            .header("Authorization", CredentialsHolder.token!!)
            .get()
            .build()
        var result = emptyList<Comment>()
        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    (HttpURLConnection.HTTP_UNAUTHORIZED) -> {
                        result = emptyList()
                        Log.println(
                            Log.WARN,
                            "loadAllComments",
                            "You are suddenly unauthorized"
                        )
                    }

                    (HttpURLConnection.HTTP_NO_CONTENT) -> {
                        result = emptyList()
                        Log.println(
                            Log.ERROR,
                            "No more comments",
                            "Already loaded all comments"
                        )
                    }

                    (HttpURLConnection.HTTP_OK) -> {
                        val typeToken = object : TypeToken<List<Comment>>() {}.type
                        result = Gson().fromJson(response.body!!.string(), typeToken)
                        Log.println(Log.INFO, "Got comments", result.size.toString())
                        for (comment in result) {
                            Log.println(Log.INFO, "comment", Gson().toJson(comment))
                        }
                    }
                }
                countDownLatch.countDown()
            }
        })
        withContext(Dispatchers.IO) {
            countDownLatch.await()
        }
        commentCache.addToCache(trackId, result)

        result
    } catch (e: Exception) {
        emptyList()
    }

    suspend fun addComment(
        trackId: String,
        content: String
    ): AddCommentError {
        val jsonString = Gson().toJson(AddCommentForm(trackId, content))
        Log.println(Log.INFO, "addComment", jsonString)
        val body = jsonString.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(Constants.BASE_URL + "comment/addComment")
            .post(body)
            .addHeader("Authorization", CredentialsHolder.token!!)
            .build()
        var result = AddCommentError.OK
        var newComment: Comment? = null
        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("addComment", "Call Failure")
                result = AddCommentError.CALL_FAILURE
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    HttpURLConnection.HTTP_OK -> {
                        val typeToken = object : TypeToken<Comment>() {}.type
                        newComment = Gson().fromJson(response.body!!.string(), typeToken)
                        Log.d("addComment", "Success")
                        Log.d("addComment", newComment.toString())
                    }

                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        Log.d("addComment", "Unauthorized")
                        result = AddCommentError.UNAUTHORIZED
                    }

                    else -> {
                        Log.d("addComment", "Unknown Response")
                        result = AddCommentError.UNKNOWN_RESPONSE
                    }
                }
                countDownLatch.countDown()
            }
        })
        withContext(Dispatchers.IO) {
            countDownLatch.await()
        }
        if (result == AddCommentError.OK) {
            commentCache.updateCache(trackId, newComment)
        }
        return result
    }
}
