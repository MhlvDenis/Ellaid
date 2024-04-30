package ru.ellaid.app.network.comment.cache

import android.util.Log
import ru.ellaid.app.data.entity.Comment
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class CommentCache {

    private val cached: MutableMap<String, MutableList<Comment>> = HashMap()
    private val cacheList: MutableList<String> = ArrayList()
    private val cacheLock: Lock = ReentrantLock()

    fun addToCache(
        trackId: String,
        comments: List<Comment>
    ) {
        if (comments.isEmpty()) {
            return
        }

        cacheLock.withLock {
            if (cacheList.contains(trackId)) {
                return
            }

            if (cacheList.size == 3) {
                Log.println(
                    Log.INFO,
                    "removed track id from comment cache",
                    cacheList.first().toString()
                )

                cached.remove(cacheList.first())
                cacheList.removeAt(0)
            }

            cached[trackId] = comments.toMutableList()
            cacheList.add(trackId)
            Log.println(Log.INFO, "added track id to comment cache", trackId)
        }
    }

    fun updateCache(trackId: String, newComment: Comment?): Boolean {
        cacheLock.withLock {
            if (!cached.contains(trackId)) {
                return false
            }

            cached[trackId]!!.add(newComment!!)
            return true
        }
    }

    fun loadFromCache(trackId: String): List<Comment>? {
        cacheLock.withLock {
            if (!cached.contains(trackId)) {
                return null
            }
            Log.println(Log.INFO, "loaded from cache", trackId)
            return cached[trackId]
        }
    }
}
