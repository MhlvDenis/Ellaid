rootProject.name = "backend"

include(
    // Services
    "auth",
    "comment",
    "playlist",
    "track",

    // Spring Boot Starters
    "common:jwt-auth-starter"
)