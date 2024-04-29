package ru.ellaid.app.exoplayer

import android.support.v4.media.MediaMetadataCompat
import ru.ellaid.app.data.entity.Track

fun MediaMetadataCompat.toSong(): Track? {
    return description?.let {
        Track(
            it.mediaId ?: "",
            it.title.toString(),
            it.subtitle.toString(),
            it.mediaUri.toString(),
            it.iconUri.toString()
        )
    }
}