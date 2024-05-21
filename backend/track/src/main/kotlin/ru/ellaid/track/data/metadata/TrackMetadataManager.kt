package ru.ellaid.track.data.metadata

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.ellaid.track.data.entity.Track
import ru.ellaid.track.data.repository.TrackRepository

private val logger = KotlinLogging.logger { }

@Component
@Transactional
@EnableScheduling
open class TrackMetadataManager(
    private val repository: TrackRepository
) {

    @Volatile
    private var _metadata: Map<String, Track> = HashMap()

    val metadata: Map<String, Track>
        get() = _metadata

    @PostConstruct
    private fun initMetadata() {
        loadMetadata()
    }

    @Scheduled(fixedRateString = "\${metadata.update-period-ms}")
    open fun updateMetadataTask() {
        loadMetadata()
    }

    private fun loadMetadata() {
        val newMetadata = repository.findAll().associateBy { track -> track.id!! }
        _metadata = newMetadata
        logger.info { "Update track metadata" }
    }
}
