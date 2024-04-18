rootProject.name = "backend"

include(
    // Services
    "auth",
    "comment",
    "eureka",
    "playlist",
    "track",

    // Spring Boot Starters
    "common:jwt-auth-starter"
)