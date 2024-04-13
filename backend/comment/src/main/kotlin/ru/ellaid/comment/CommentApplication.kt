package ru.ellaid.comment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class CommentApplication

fun main(args: Array<String>) {
	runApplication<CommentApplication>(*args)
}
