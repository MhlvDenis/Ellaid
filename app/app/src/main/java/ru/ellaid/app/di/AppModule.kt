package ru.ellaid.app.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ellaid.app.R
import ru.ellaid.app.adapters.CommentAdapter
import ru.ellaid.app.adapters.SearchAdapter
import ru.ellaid.app.adapters.SwipeSongAdapter
import ru.ellaid.app.exoplayer.MusicServiceConnection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    ): MusicServiceConnection = MusicServiceConnection(context)

    @Singleton
    @Provides
    fun provideSwipeSongAdapter(): SwipeSongAdapter = SwipeSongAdapter()

    @Provides
    fun provideCommentAdapter(): CommentAdapter = CommentAdapter()

    @Singleton
    @Provides
    fun provideSearchAdapter(
        glide: RequestManager
    ): SearchAdapter = SearchAdapter(glide)

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )
}