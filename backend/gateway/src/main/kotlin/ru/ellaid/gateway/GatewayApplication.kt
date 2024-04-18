package ru.ellaid.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
open class GatewayApplication

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}
