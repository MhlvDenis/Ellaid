package ru.ellaid.track.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ellaid.track.data.entity.Track
import ru.ellaid.track.exception.DuplicateTrackUrlException
import ru.ellaid.track.exception.TrackNotFoundException
import ru.ellaid.track.rest.form.CreateTrackForm
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
        ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping
    fun postTrack(
        @RequestBody createTrackForm: CreateTrackForm
    ): ResponseEntity<Track> = try {
        ResponseEntity(
            trackService.createTrack(
                createTrackForm.name,
                createTrackForm.author,
                createTrackForm.musicUrl,
                createTrackForm.coverUrl
            ),
            HttpStatus.CREATED
        )
    } catch (e: DuplicateTrackUrlException) {
        ResponseEntity(HttpStatus.CONFLICT)
    }
}
