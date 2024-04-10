package ru.ellaid.playlist.data.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.ellaid.playlist.data.entity.Playlist

interface PlaylistRepository: MongoRepository<Playlist, String> {

    fun findAllByUserId(userId: String): List<Playlist>

    fun findByTitleAndUserId(title: String, userId: String): Playlist?
}
