package ru.ellaid.app.network.playlist.status

enum class CreatePlaylistStatus {
    OK,
    CALL_FAILURE,
    UNAUTHORIZED,
    PLAYLIST_ALREADY_EXISTS,
    UNKNOWN_RESPONSE,
}
