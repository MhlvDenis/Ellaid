package ru.ellaid.comment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
open class CommentApplication

fun main(args: Array<String>) {
	runApplication<CommentApplication>(*args)
}
