package ru.ellaid.storage.service.url

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class UrlTransformer(
    @Value("\${host.url}")
    private val hostUrl: String,
    @Value("\${minio.url}")
    private val minioUrl: String,
) {

    fun toExternalLink(
        url: String
    ): String = "${hostUrl}/storage-api/download${url.substring(minioUrl.length)}"
}