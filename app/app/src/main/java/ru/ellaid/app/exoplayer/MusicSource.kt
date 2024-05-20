package ru.ellaid.app.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.Builder
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import ru.ellaid.app.exoplayer.State.STATE_CREATED
import ru.ellaid.app.exoplayer.State.STATE_ERROR
import ru.ellaid.app.exoplayer.State.STATE_INITIALIZED
import ru.ellaid.app.exoplayer.State.STATE_INITIALIZING
import ru.ellaid.app.network.track.TrackClient
import javax.inject.Inject

class MusicSource @Inject constructor(
    private val trackClient: TrackClient,
) {
    var tracks = emptyList<MediaMetadataCompat>()

    fun fetchMediaData(
        trackIds: List<String>,
    ) {
        state = STATE_INITIALIZING
        trackClient.fetchTracks(trackIds) { tracksMetadata ->
            tracks = tracksMetadata.map { track ->
                Builder()
                    .putString(METADATA_KEY_ARTIST, track.author)
                    .putString(METADATA_KEY_MEDIA_ID, track.id)
                    .putString(METADATA_KEY_TITLE, track.name)
                    .putString(METADATA_KEY_DISPLAY_TITLE, track.name)
                    .putString(METADATA_KEY_DISPLAY_ICON_URI, track.coverUrl)
                    .putString(METADATA_KEY_MEDIA_URI, track.musicUrl)
                    .putString(METADATA_KEY_ALBUM_ART_URI, track.coverUrl)
                    .putString(METADATA_KEY_DISPLAY_SUBTITLE, track.author)
                    .putString(METADATA_KEY_DISPLAY_DESCRIPTION, track.author)
                    .build()
            }

            state = STATE_INITIALIZED
        }
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        tracks.forEach { song ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = tracks.map { song ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(song.description.title)
            .setSubtitle(song.description.subtitle)
            .setMediaId(song.description.mediaId)
            .setIconUri(song.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean =
        (state == STATE_CREATED || state == STATE_INITIALIZING).also { isReady ->
            if (isReady) {
                action(state == STATE_INITIALIZED)
            } else {
                onReadyListeners += action
            }
        }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}