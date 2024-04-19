package ru.ellaid.gateway.filter

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

private val logger = KotlinLogging.logger { }

@Component
class TokenValidationFilterFactory : AbstractGatewayFilterFactory<TokenValidationFilterFactory.Config>() {

    companion object {
        private const val DOMAIN = "http://gateway:8080"
        private const val VALIDATION_ENDPOINT = "/auth/validate"
        private const val TOKEN_PARAM = "token"
    }

    private val client = WebClient.create(DOMAIN)

    override fun apply(
        config: Config?
    ): GatewayFilter = GatewayFilter { exchange, chain ->
        val request = exchange.request
        val terminateResponse: () -> Mono<Void> = {
            exchange.response.apply {
                statusCode = HttpStatus.UNAUTHORIZED
            }.setComplete()
        }

        val authHeader = request.headers[HttpHeaders.AUTHORIZATION]
        if (authHeader.isNullOrEmpty()) {
            logger.info { "Terminate request ${request.id}: auth token is missing" }
            return@GatewayFilter terminateResponse()
        }

        val token = authHeader[0]
        return@GatewayFilter client.get()
            .uri { it.path(VALIDATION_ENDPOINT).queryParam(TOKEN_PARAM, token).build() }
            .retrieve()
            .bodyToMono<ValidationResponse>()
            .flatMap { response ->
                return@flatMap if (response.valid) {
                    chain.filter(exchange)
                } else {
                    logger.info { "Terminate request ${request.id}: invalid auth token" }
                    terminateResponse()
                }
            }
    }

    class Config
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class ValidationResponse(
    val valid: Boolean
)
