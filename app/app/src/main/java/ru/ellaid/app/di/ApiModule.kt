package ru.ellaid.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ru.ellaid.app.network.auth.AuthClient
import ru.ellaid.app.network.comment.CommentClient
import ru.ellaid.app.network.playlist.PlaylistClient
import ru.ellaid.app.network.track.TrackClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApiClient(): OkHttpClient =
        OkHttpClient.Builder()
            .pingInterval(1, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false).build()

    @Provides
    @Singleton
    fun provideAuthClient(
        client: OkHttpClient,
    ): AuthClient = AuthClient(client)

    @Provides
    @Singleton
    fun provideCommentClient(
        client: OkHttpClient,
    ): CommentClient = CommentClient(client)

    @Provides
    @Singleton
    fun providePlaylistClient(
        client: OkHttpClient,
    ): PlaylistClient = PlaylistClient(client)

    @Provides
    @Singleton
    fun provideTrackClient(
        client: OkHttpClient,
    ): TrackClient = TrackClient(client)
}