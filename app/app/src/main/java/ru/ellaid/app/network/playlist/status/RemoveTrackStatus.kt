package ru.ellaid.app.network.playlist.status

enum class RemoveTrackStatus {
    OK,
    CALL_FAILURE,
    UNAUTHORIZED,
    NOT_FOUND,
    UNKNOWN_RESPONSE,
}