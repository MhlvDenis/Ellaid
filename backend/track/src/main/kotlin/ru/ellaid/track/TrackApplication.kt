package ru.ellaid.track

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
open class TrackApplication

fun main(args: Array<String>) {
    runApplication<TrackApplication>(*args)
}
