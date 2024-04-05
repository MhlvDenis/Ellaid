package ru.ellaid.track.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ellaid.track.entity.Track
import ru.ellaid.track.exception.TrackNotFoundException
import ru.ellaid.track.form.TrackDto
import ru.ellaid.track.service.TrackService

@RestController
@RequestMapping("/track")
class TrackController(
    private val trackService: TrackService
) {

    @GetMapping
    fun getTrack(
        @RequestParam id: String
    ): ResponseEntity<Track>
    = try {
        ResponseEntity(trackService.getTrack(id), HttpStatus.OK)
    } catch (e: TrackNotFoundException) {
        ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @PostMapping
    fun postTrack(
        @RequestBody trackDto: TrackDto
    ): ResponseEntity<String>
    = ResponseEntity(trackService.addTrack(trackDto).id, HttpStatus.CREATED)
}
