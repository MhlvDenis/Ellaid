package ru.ellaid.storage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
open class StorageApplication

fun main(args: Array<String>) {
    runApplication<StorageApplication>(*args)
}