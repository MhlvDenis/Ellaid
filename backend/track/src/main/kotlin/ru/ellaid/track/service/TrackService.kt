package ru.ellaid.track.service

import org.springframework.stereotype.Service
import ru.ellaid.track.entity.Track
import ru.ellaid.track.exception.TrackNotFoundException
import ru.ellaid.track.form.TrackDto
import ru.ellaid.track.repository.TracksRepository
import java.util.concurrent.ConcurrentHashMap

@Service
class TrackService(
    private val repository: TracksRepository
) {

    private val cache: MutableMap<String, Track> = ConcurrentHashMap()

    fun getTrack(id: String): Track {
        if (id !in cache) {
            cache[id] = repository.findById(id).orElseThrow { TrackNotFoundException() }
        }

        return cache[id]!!
    }

    fun addTrack(trackDto: TrackDto): Track = repository.save(
        Track(
            name = trackDto.name,
            author = trackDto.author,
            musicUrl = trackDto.musicUrl,
            coverUrl = trackDto.coverUrl
        )
    )
}
