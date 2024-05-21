package ru.ellaid.playlist.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.ellaid.playlist.data.entity.Playlist
import ru.ellaid.playlist.data.repository.PlaylistRepository
import ru.ellaid.playlist.exception.DuplicatePlaylistException
import ru.ellaid.playlist.exception.DuplicateTrackException
import ru.ellaid.playlist.exception.PlaylistNotFoundException
import ru.ellaid.playlist.exception.TrackNotFoundException

private val logger = KotlinLogging.logger { }

@Service
@Transactional
open class PlaylistService(
    private val repository: PlaylistRepository
) {

    fun getPlaylist(playlistId: String): Playlist =
        repository.findById(playlistId).orElseThrow {
            logger.error { "Playlist $playlistId not found" }
            PlaylistNotFoundException()
        }

    fun getPlaylists(userId: String): List<Playlist> =
        repository.findAllByUserId(userId)

    fun createPlaylist(
        title: String,
        userId: String
    ): Playlist {
        if (repository.findByTitleAndUserId(title, userId) != null) {
            logger.error { "User $userId already has playlist $title" }
            throw DuplicatePlaylistException()
        }

        return repository.save(Playlist(title, userId))
    }

    fun deletePlaylist(playlistId: String): Playlist =
        getPlaylist(playlistId).also {
            repository.deleteById(playlistId)
        }

    fun addTrackToPlaylist(
        playlistId: String,
        trackId: String
    ): Playlist =
        getPlaylist(playlistId).apply {
            if (trackId in trackIds) {
                logger.error { "Playlist $playlistId already has track $trackId" }
                throw DuplicateTrackException()
            }

            trackIds.add(trackId)
        }.also {
            repository.save(it)
        }

    fun removeTrackFromPlaylist(
        playlistId: String,
        trackId: String
    ): Playlist =
        getPlaylist(playlistId).apply {
            if (trackId !in trackIds) {
                logger.error { "Playlist $playlistId has no track $trackId" }
                throw TrackNotFoundException()
            }

            trackIds.remove(trackId)
        }.also {
            repository.save(it)
        }
}
