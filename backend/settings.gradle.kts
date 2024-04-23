rootProject.name = "backend"

include(
    // Services
    "auth",
    "comment",
    "playlist",
    "storage",
    "track",

    // Infrastructure
    "eureka",
    "gateway",

    // Spring Boot Starters
    "common:jwt-auth-starter"
)