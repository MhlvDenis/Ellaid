package ru.ellaid.track.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.ellaid.track.entity.Track

interface TracksRepository: MongoRepository<Track, String>
