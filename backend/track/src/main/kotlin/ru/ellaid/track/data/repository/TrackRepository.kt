package ru.ellaid.track.data.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.ellaid.track.data.entity.Track

interface TrackRepository: MongoRepository<Track, String> {

    fun findByMusicUrl(url: String): Track?
}
