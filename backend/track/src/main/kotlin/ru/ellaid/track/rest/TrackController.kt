package ru.ellaid.track.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ellaid.track.data.entity.Track
import ru.ellaid.track.exception.DuplicateTrackUrlException
import ru.ellaid.track.exception.TrackNotFoundException
import ru.ellaid.track.rest.dto.TrackDto
import ru.ellaid.track.service.SearchService
import ru.ellaid.track.service.TrackService

@RestController
@RequestMapping("/track")
class TrackController(
    private val trackService: TrackService,
    private val searchService: SearchService,
) {

    @GetMapping
    fun getTrack(
        @RequestParam id: String
    ): ResponseEntity<Track> = try {
        ResponseEntity(searchService.getTrack(id), HttpStatus.OK)
    } catch (e: TrackNotFoundException) {
        ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @PostMapping
    fun postTrack(
        @RequestBody trackDto: TrackDto
    ): ResponseEntity<String> = try {
        ResponseEntity(trackService.addTrack(trackDto).id, HttpStatus.CREATED)
    } catch (e: DuplicateTrackUrlException) {
        ResponseEntity(HttpStatus.CONFLICT)
    }
}
