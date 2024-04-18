package ru.ellaid.gateway.route

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RouteConfig {

    @Bean
    open fun ellaidRoute(
        builder: RouteLocatorBuilder
    ): RouteLocator =
        builder.routes()
            .route("auth_route") { route ->
                route.path("/auth/**").uri("lb://auth")
            }
            .route("comment_route") { route ->
                route.path("/comment/**", "/comments").uri("lb://comment")
            }
            .route("playlist_route") { route ->
                route.path("/playlist/**", "/playlists").uri("lb://playlist")
            }
            .route("track_route") { route ->
                route.path("/track").uri("lb://track")
            }
            .build()
}
