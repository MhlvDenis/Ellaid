package ru.ellaid.playlist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
open class PlaylistApplication

fun main(args: Array<String>) {
	runApplication<PlaylistApplication>(*args)
}
