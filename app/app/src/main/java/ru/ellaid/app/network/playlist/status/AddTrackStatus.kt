package ru.ellaid.app.network.playlist.status

enum class AddTrackStatus {
    OK,
    CALL_FAILURE,
    UNAUTHORIZED,
    TRACK_ALREADY_ADDED,
    UNKNOWN_RESPONSE,
}
