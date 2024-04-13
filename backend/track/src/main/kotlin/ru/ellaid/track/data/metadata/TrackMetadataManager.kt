package ru.ellaid.track.data.metadata

import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.ellaid.track.data.entity.Track
import ru.ellaid.track.data.repository.TrackRepository
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock

@Component
@EnableAsync
@EnableScheduling
open class TrackMetadataManager(
    private val repository: TrackRepository
) {

    private val metadataLock: ReadWriteLock = ReentrantReadWriteLock()
    private val readLock = metadataLock.readLock()
    private val writeLock = metadataLock.writeLock()

    private var _metadata: Map<String, Track> = HashMap()
    val metadata: Map<String, Track>
        get() {
            readLock.withLock {
                return _metadata
            }
        }

    @PostConstruct
    private fun initMetadata() {
        loadMetadata()
    }

    @Async
    @Scheduled(fixedRateString = "\${metadata.update-period-ms}")
    open fun updateMetadataTask() {
        loadMetadata()
    }

    private fun loadMetadata() {
        val newMetadata = repository.findAll().associateBy { track -> track.id!! }
        writeLock.withLock {
            _metadata = newMetadata
        }
    }
}
