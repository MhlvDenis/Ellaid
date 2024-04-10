package ru.ellaid.track.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import ru.ellaid.track.algorithm.SearchAlgorithm
import ru.ellaid.track.data.entity.Track
import ru.ellaid.track.exception.TrackNotFoundException
import ru.ellaid.track.metadata.TrackMetadataManager

private val logger = KotlinLogging.logger { }

@Service
class SearchService(
    private val metadataManager: TrackMetadataManager,
    private val searchAlgorithm: SearchAlgorithm
) {

    fun getTrack(id: String): Track =
        metadataManager.metadata[id] ?: throw TrackNotFoundException().also {
            logger.error { "Not found track by id $id" }
        }

    fun searchByPattern(pattern: String): List<Track> =
        searchAlgorithm.perform(
            metadataManager.metadata.values.toList(),
            pattern
        )
}
