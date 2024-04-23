dependencies {
    implementation(project(":common:jwt-auth-starter"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // MinIO
    implementation("io.minio:minio:8.5.9")

    // Logger
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")

    // Service Discovery
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
}