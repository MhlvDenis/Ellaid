package ru.ellaid.track

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrackApplication

fun main(args: Array<String>) {
    runApplication<TrackApplication>(*args)
}