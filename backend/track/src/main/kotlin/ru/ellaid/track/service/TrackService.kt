package ru.ellaid.track.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import ru.ellaid.track.data.entity.Track
import ru.ellaid.track.data.repository.TrackRepository
import ru.ellaid.track.exception.DuplicateTrackUrlException
import ru.ellaid.track.rest.dto.TrackDto

private val logger = KotlinLogging.logger { }

@Service
class TrackService(
    private val repository: TrackRepository
) {

    fun addTrack(trackDto: TrackDto): Track {
        if (repository.findByMusicUrl(trackDto.musicUrl) != null) {
            logger.error { "Track with url ${trackDto.musicUrl} already exists" }
            throw DuplicateTrackUrlException()
        }

        return repository.save(
            Track(
                name = trackDto.name,
                author = trackDto.author,
                musicUrl = trackDto.musicUrl,
                coverUrl = trackDto.coverUrl
            )
        )
    }
}
