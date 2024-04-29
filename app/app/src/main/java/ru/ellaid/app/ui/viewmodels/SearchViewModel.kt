package ru.ellaid.app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ellaid.app.data.entity.Track
import ru.ellaid.app.exoplayer.MusicService
import ru.ellaid.app.other.Resource

class SearchViewModel : ViewModel() {
    private val _mediaItems = MutableLiveData<Resource<List<Track>>>()
    val mediaItems: LiveData<Resource<List<Track>>> = _mediaItems

    init {
        _mediaItems.postValue(Resource.success(emptyList()))
    }

    fun provideSearch(request: String) {
        if (request.isEmpty()) {
            _mediaItems.postValue(Resource.success(emptyList()))
        } else {
            MusicService.requestSearch(request) { songs ->
                _mediaItems.postValue(Resource.success(songs))
            }
        }
    }
}