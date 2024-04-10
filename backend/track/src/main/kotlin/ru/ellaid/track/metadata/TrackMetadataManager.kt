package ru.ellaid.track.metadata

import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.ellaid.track.data.entity.Track
import ru.ellaid.track.data.repository.TrackRepository

@Component
@EnableAsync
@EnableScheduling
class TrackMetadataManager(
    private val repository: TrackRepository
) {
    @Volatile
    private var _metadata: Map<String, Track> = HashMap()
    val metadata: Map<String, Track>
        get() = _metadata

    @PostConstruct
    private fun initMetadata() {
        _metadata = loadMetadata()
    }

    @Async
    @Scheduled(fixedRateString = "\${metadata.update-period-ms}")
    fun updateMetadata() {
        _metadata = loadMetadata()
    }

    private fun loadMetadata(): Map<String, Track> =
        repository.findAll().associateBy { track -> track.id!! }
}
