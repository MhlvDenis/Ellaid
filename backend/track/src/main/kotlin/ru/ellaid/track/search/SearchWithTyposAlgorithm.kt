package ru.ellaid.track.search

import ru.ellaid.track.data.entity.Track
import kotlin.math.abs
import kotlin.math.min

class SearchWithTyposAlgorithm(
    private val returnLimit: Long,
): SearchAlgorithm {

    override fun perform(
        tracks: Collection<Track>,
        pattern: String
    ): List<Track> {
        return tracks.stream()
            .map { vectorTrack(it, pattern) }
            .sorted { a, b ->
                when {
                    a.isSubstring && b.isSubstring -> 0
                    a.isSubstring -> -1
                    b.isSubstring -> 1
                    a.distance < b.distance -> -1
                    a.distance > b.distance -> 1
                    else -> 0
                }
            }
            .limit(returnLimit)
            .map { it.track }
            .toList()
    }

    private data class VectorizedTrack(
        val track: Track,
        val distance: Int,
        val isSubstring: Boolean,
    )

    private fun vectorTrack(track: Track, rawPattern: String): VectorizedTrack {
        val pattern = rawPattern.trim().lowercase()
        val nameAuthorPattern = (track.name + track.author).lowercase().filter { !it.isWhitespace() }
        val authorNamePattern = (track.author + track.name).lowercase().filter { !it.isWhitespace() }
        val standard = abs(nameAuthorPattern.length - pattern.length)

        val natDistance = levenshteinDistance(pattern, nameAuthorPattern)
        val antDistance = levenshteinDistance(pattern, authorNamePattern)

        return if (natDistance == standard || antDistance == standard) {
            VectorizedTrack(track, 0, true)
        } else {
            VectorizedTrack(
                track,
                min(natDistance - standard, antDistance - standard),
                false
            )
        }
    }

    private fun levenshteinDistance(x: String, y: String): Int {
        val dp = Array(x.length + 1) { IntArray(y.length + 1) }

        for (i in 0..x.length) {
            for (j in 0..y.length) {
                when {
                    i == 0 -> dp[i][j] = j
                    j == 0 -> dp[i][j] = i
                    else -> dp[i][j] = minOf(
                        dp[i - 1][j - 1] + if (x[i - 1] == y[j - 1]) 0 else 1,
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1,
                    )
                }
            }
        }

        return dp[x.length][y.length]
    }
}
