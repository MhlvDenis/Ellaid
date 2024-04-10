package ru.ellaid.playlist.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ellaid.playlist.data.entity.Playlist
import ru.ellaid.playlist.exception.DuplicatePlaylistException
import ru.ellaid.playlist.exception.DuplicateTrackException
import ru.ellaid.playlist.exception.PlaylistNotFoundException
import ru.ellaid.playlist.exception.TrackNotFoundException
import ru.ellaid.playlist.rest.form.CreatePlaylistForm
import ru.ellaid.playlist.rest.form.HandleTrackForm
import ru.ellaid.playlist.service.PlaylistService

@RestController
class PlaylistController(
    private val playlistService: PlaylistService
) {

    @GetMapping("/playlist")
    fun getPlaylist(
        @RequestParam id: String
    ): ResponseEntity<Playlist> = try {
        ResponseEntity(playlistService.getPlaylist(id), HttpStatus.OK)
    } catch (e: PlaylistNotFoundException) {
        ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/playlists")
    fun getPlaylists(
        @RequestParam userId: String
    ): ResponseEntity<List<Playlist>> =
        ResponseEntity(playlistService.getPlaylists(userId), HttpStatus.OK)

    @PostMapping("/playlist")
    fun createPlaylist(
        @RequestBody createPlaylistForm: CreatePlaylistForm
    ): ResponseEntity<Playlist> = try {
        ResponseEntity(
            playlistService.createPlaylist(
                createPlaylistForm.title,
                createPlaylistForm.userId
            ),
            HttpStatus.CREATED
        )
    } catch (e: DuplicatePlaylistException) {
        ResponseEntity(HttpStatus.CONFLICT)
    }

    @DeleteMapping("/playlist")
    fun deletePlaylist(
        @RequestParam id: String
    ): ResponseEntity<Playlist> = try {
        ResponseEntity(playlistService.deletePlaylist(id), HttpStatus.OK)
    } catch (e: PlaylistNotFoundException) {
        ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PatchMapping("/playlist/add-track")
    fun addTrackToPlaylist(
        @RequestBody handleTrackForm: HandleTrackForm
    ): ResponseEntity<Playlist> = try {
        ResponseEntity(
            playlistService.addTrackToPlaylist(
                handleTrackForm.playlistId,
                handleTrackForm.trackId
            ),
            HttpStatus.OK
        )
    } catch (e: DuplicateTrackException) {
        ResponseEntity(HttpStatus.CONFLICT)
    } catch (e: PlaylistNotFoundException) {
        ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PatchMapping("/playlist/remove-track")
    fun removeTrackFromPlaylist(
        @RequestBody handleTrackForm: HandleTrackForm
    ): ResponseEntity<Playlist> = try {
        ResponseEntity(
            playlistService.removeTrackFromPlaylist(
                handleTrackForm.playlistId,
                handleTrackForm.trackId
            ),
            HttpStatus.OK
        )
    } catch (e: TrackNotFoundException) {
        ResponseEntity(HttpStatus.NOT_FOUND)
    } catch (e: PlaylistNotFoundException) {
        ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
