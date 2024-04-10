package ru.ellaid.track.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import ru.ellaid.track.data.entity.Track
import ru.ellaid.track.data.repository.TrackRepository
import ru.ellaid.track.exception.DuplicateTrackUrlException

private val logger = KotlinLogging.logger { }

@Service
class TrackService(
    private val repository: TrackRepository
) {

    fun createTrack(
        name: String,
        author: String,
        musicUrl: String,
        coverUrl: String
    ): Track {
        if (repository.findByMusicUrl(musicUrl) != null) {
            logger.error { "Track with url $musicUrl already exists" }
            throw DuplicateTrackUrlException()
        }

        return repository.save(
            Track(name, author, musicUrl, coverUrl)
        )
    }
}
