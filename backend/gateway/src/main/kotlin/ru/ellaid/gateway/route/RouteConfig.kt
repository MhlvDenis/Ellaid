package ru.ellaid.gateway.route

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import ru.ellaid.gateway.filter.TokenValidationFilterFactory

@Configuration
open class RouteConfig {

    @Bean
    open fun ellaidRoute(
        builder: RouteLocatorBuilder,
        tokenValidationFilterFactory: TokenValidationFilterFactory
    ): RouteLocator {

        val createValidationFilter: () -> GatewayFilter = {
            tokenValidationFilterFactory.apply(TokenValidationFilterFactory.Config())
        }

        return builder.routes()
            // Auth Service
            .route("auth_route") { route ->
                route.path("/auth/**").uri("lb://auth")
            }
            // Comment Service
            .route("affect_comment_route") { route ->
                route.path("/comment/**").and()
                    .method(HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH)
                    .filters { it.filter(createValidationFilter()) }
                    .uri("lb://comment")
            }
            .route("fetch_comments_route") { route ->
                route.path("/comments").and()
                    .method(HttpMethod.GET)
                    .uri("lb://comment")
            }
            // Playlist Service
            .route("affect_playlist_route") { route ->
                route.path("/playlist/**").and()
                    .method(HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH)
                    .filters { it.filter(createValidationFilter()) }
                    .uri("lb://playlist")
            }
            .route("fetch_playlist_route") { route ->
                route.path("/playlist").and()
                    .method(HttpMethod.GET)
                    .uri("lb://playlist")
            }
            .route("fetch_playlists_route") { route ->
                route.path("/playlists").and()
                    .method(HttpMethod.GET)
                    .uri("lb://playlist")
            }
            // Track Service
            .route("affect_track_route") { route ->
                route.path("/track").and()
                    .method(HttpMethod.POST)
                    .filters { it.filter(createValidationFilter()) }
                    .uri("lb://track")
            }
            .route("fetch_track_route") { route ->
                route.path("/track").and()
                    .method(HttpMethod.GET)
                    .uri("lb://track")
            }
            // Storage
            .route("storage_upload_route") { route ->
                route.path("/storage-api/upload").uri("lb://storage")
            }
            .route("storage_download_route") { route ->
                route.path("/storage-api/download/**")
                    .filters { it.stripPrefix(2) }
                    .uri("http://minio:9000")
            }
            .build()
    }
}
