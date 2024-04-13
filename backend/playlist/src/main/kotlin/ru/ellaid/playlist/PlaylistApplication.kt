package ru.ellaid.playlist

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class PlaylistApplication

fun main(args: Array<String>) {
	runApplication<PlaylistApplication>(*args)
}
