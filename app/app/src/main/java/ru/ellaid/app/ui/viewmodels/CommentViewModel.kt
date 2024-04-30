package ru.ellaid.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ellaid.app.common.Resource
import ru.ellaid.app.data.entity.Comment
import ru.ellaid.app.exoplayer.MusicServiceConnection
import ru.ellaid.app.network.comment.CommentClient
import ru.ellaid.app.network.comment.status.AddCommentStatus
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    musicServiceConnection: MusicServiceConnection,
    private val commentClient: CommentClient,
) : ViewModel() {

    private val _commentItems = MutableLiveData<Resource<List<Comment>>>()
    val commentItems: LiveData<Resource<List<Comment>>> = _commentItems

    val curPlayingSong = musicServiceConnection.curPlayingSong

    fun loadComments(trackId: String) {
        _commentItems.postValue(Resource.loading(null))
        commentClient.fetchComments(trackId) { result ->
            _commentItems.postValue(Resource.success(result.reversed()))
        }
    }

    fun uploadCommentAndUpdate(trackId: String, content: String) {
        _commentItems.postValue(Resource.loading(null))
        commentClient.addComment(trackId, content) { result ->
            if (result != AddCommentStatus.OK) {
                Log.println(
                    Log.ERROR,
                    "CommentViewModel",
                    "Failed to add comment to track $trackId"
                )
            }
        }
        loadComments(trackId)
    }
}
