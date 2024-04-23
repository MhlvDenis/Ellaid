package ru.ellaid.track.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.ellaid.track.data.entity.Track
import ru.ellaid.track.service.SearchService

@RestController
@RequestMapping("/search")
class SearchController(
    private val searchService: SearchService,
) {

    @GetMapping
    fun searchByPattern(
        @RequestParam("pattern") pattern: String
    ): ResponseEntity<List<Track>> =
        ResponseEntity(
            searchService.searchByPattern(pattern),
            HttpStatus.OK
        )
}
