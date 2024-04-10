package ru.ellaid.playlist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PlaylistApplication

fun main(args: Array<String>) {
	runApplication<PlaylistApplication>(*args)
}
