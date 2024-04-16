dependencies {
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-autoconfigure")

    // JWT
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Logger
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
