package ru.ellaid.storage.service

import io.github.oshai.kotlinlogging.KotlinLogging
import io.minio.*
import io.minio.http.Method
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.ellaid.storage.exception.StorageException
import ru.ellaid.storage.service.url.UrlTransformer
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger { }

@Service
class StorageService(
    private val client: MinioClient,
    private val urlTransformer: UrlTransformer,
    @Value("\${minio.bucket}")
    private val bucket: String,
) {

    fun uploadFile(
        name: String,
        content: ByteArray
    ): String = try {
        client.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(name)
                .stream(content.inputStream(), content.size.toLong(), -1)
                .build()
        )

        val url = client.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .expiry(TimeUnit.HOURS.toSeconds(12).toInt())
                .bucket(bucket)
                .`object`(name)
                .build()
        )

        urlTransformer.toExternalLink(url)
    } catch (e: Exception) {
        logger.error { "Error during file upload: $e" }
        throw StorageException()
    }

    @PostConstruct
    fun init() {
        createBucketIfNotExists(bucket)
    }

    private fun createBucketIfNotExists(
        name: String
    ) {
        val isBucketExists = client.bucketExists(
            BucketExistsArgs.builder()
                .bucket(name)
                .build()
        )

        if (!isBucketExists) {
            client.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(name)
                    .build()
            )
        }
    }
}
