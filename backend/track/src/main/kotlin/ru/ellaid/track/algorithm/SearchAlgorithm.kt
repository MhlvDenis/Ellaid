package ru.ellaid.track.algorithm

import ru.ellaid.track.data.entity.Track

interface SearchAlgorithm {

    fun perform(tracks: Collection<Track>, pattern: String): List<Track>
}