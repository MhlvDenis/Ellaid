package ru.ellaid.storage.rest

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import ru.ellaid.storage.exception.StorageException
import ru.ellaid.storage.service.StorageService

private val logger = KotlinLogging.logger { }

@RestController
class StorageController(
    private val storageService: StorageService
) {

    @PostMapping(path = ["/storage-api/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(
        @RequestParam("file")
        file: MultipartFile
    ): ResponseEntity<String> = try {
        if (file.originalFilename != null) {
            ResponseEntity(
                storageService.uploadFile(file.originalFilename!!, file.bytes),
                HttpStatus.CREATED
            )
        } else {
            logger.error { "Filename isn't provided" }
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    } catch (e: StorageException) {
        ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
