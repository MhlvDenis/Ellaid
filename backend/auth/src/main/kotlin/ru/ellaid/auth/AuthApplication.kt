package ru.ellaid.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
open class AuthApplication

fun main(args: Array<String>) {
    runApplication<AuthApplication>(*args)
}
