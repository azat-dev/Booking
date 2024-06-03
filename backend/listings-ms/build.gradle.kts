import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Target
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.MountableFile

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.postgresql:postgresql:42.5.0")
        classpath("org.testcontainers:postgresql:1.19.8")
        classpath("org.jooq:jooq-codegen:3.19.9")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
    }
}

plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.openapi.generator") version "7.6.0"
}

group = "com.azat4dev.booking"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":shared"))
    implementation(project(":apiclient"))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.springframework.security:spring-security-test")

    // Minio
    implementation("io.minio:minio:8.5.10")
    testImplementation("io.minio:minio:8.5.10")
    testImplementation("org.testcontainers:minio:1.19.8")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Database
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.testcontainers:postgresql:1.19.8")


    // Kafka
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.testcontainers:testcontainers:1.19.8")
    testImplementation("org.testcontainers:junit-jupiter:1.19.8")
    testImplementation("org.testcontainers:kafka")

    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    // OpenAPI Generator
    implementation("io.swagger.core.v3:swagger-annotations:2.2.21")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("io.swagger.core.v3:swagger-models:2.2.21")

    // JOOQ Dependencies
    implementation("org.jooq:jooq:3.19.9")
    implementation("org.jooq:jooq-meta:3.19.9")
    implementation("org.jooq:jooq-codegen:3.19.9")

    testImplementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.1")

    // Java Faker
    testImplementation("com.github.javafaker:javafaker:1.0.2") {
        exclude(group = "org.yaml", module = "snakeyaml")
    }

    // OkHttp
    testImplementation("com.squareup.okhttp3:okhttp:4.9.3")
}

if (hasProperty("buildScan")) {
    extensions.findByName("buildScan")?.withGroovyBuilder {
        setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
        setProperty("termsOfServiceAgree", "yes")
    }
}
tasks.named<Test>("test") {
    useJUnitPlatform()
}

sourceSets {
    named("main") {
        java {
            srcDirs(
                "src/main/java",
                "$buildDir/generated/server/src/main/java",
                "$buildDir/generated/events/src/main/java",
                "$buildDir/generated/jooq"
            )
        }
    }
}


// Generate JOOQ classes

tasks.register<GenerateTask>("generateServerApi") {
    generatorName.set("spring")
    library.set("spring-boot")
    inputSpec.set("$rootDir/../specs/listings/openapi.yaml")
    outputDir.set("$buildDir/generated/server")
    invokerPackage.set("com.azat4dev.booking.listingsms.generated.server.base")
    apiPackage.set("com.azat4dev.booking.listingsms.generated.server.api")
    modelPackage.set("com.azat4dev.booking.listingsms.generated.server.model")
    packageName.set("com.azat4dev.booking.listingsms.generated.server")
    ignoreFileOverride.set(".openapi-generator-ignore")
    configOptions.set(
        mapOf(
            "useOptional" to "true",
            "openApiNullable" to "false",
            "interfaceOnly" to "false",
            "additionalModelTypeAnnotations" to "@lombok.Builder(toBuilder = true)\n@lombok.AllArgsConstructor\n@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)",
            "generatedConstructorWithRequiredArgs" to "false",
            "useTags" to "true",
            "dateLibrary" to "java8-localdatetime",
            "generateConstructorWithAllArgs" to "false",
            "serializationLibrary" to "jackson",
            "useSpringBoot3" to "true",
            "delegatePattern" to "true",
            "generateBuilders" to "false",
            "useSpringController" to "false"
        )
    )
}

tasks.register<GenerateTask>("generateApiClient") {
    generatorName.set("java")
    library.set("feign")
    inputSpec.set("$rootDir/../specs/listings/openapi.yaml")
    outputDir.set("$buildDir/generated/client")
    invokerPackage.set("com.azat4dev.booking.listingsms.generated.client.base")
    apiPackage.set("com.azat4dev.booking.listingsms.generated.client.api")
    modelPackage.set("com.azat4dev.booking.listingsms.generated.client.model")
    packageName.set("com.azat4dev.booking.listingsms.generated.client")
    ignoreFileOverride.set(".openapi-generator-ignore")
    configOptions.set(
        mapOf(
            "useOptional" to "true",
            "generateConstructorWithAllArgs" to "true",
            "generateBuilders" to "true",
            "useTags" to "true"
        )
    )
}

tasks.register<GenerateTask>("generateDomainEventsDTO") {
    generatorName.set("spring")
    library.set("spring-boot")
    inputSpec.set("$rootDir/../specs/events/listings/openapi.yaml")
    outputDir.set("$buildDir/generated/events")
    modelPackage.set("com.azat4dev.booking.listingsms.generated.events.dto")
    ignoreFileOverride.set(".openapi-generator-ignore")
    templateDir.set("$rootDir/../specs/custom_templates/spring")
    modelNameSuffix.set("DTO")
    configOptions.set(
        mapOf(
            "useOptional" to "true",
            "openApiNullable" to "false",
            "interfaceOnly" to "true",

            "additionalModelTypeAnnotations" to "@lombok.Builder(toBuilder = true)\n@lombok.AllArgsConstructor\n@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown=true)",
            "generatedConstructorWithRequiredArgs" to "false",
            "generateSupportingFiles" to "false",
            "useTags" to "true",
            "generateSupportingFiles" to "false",

            "delegatePattern" to "true",

            "dateLibrary" to "java8-localdatetime",
            "generateConstructorWithAllArgs" to "false",

            "serializationLibrary" to "jackson",
            "generateBuilders" to "false",
            "useSpringController" to "false",
            "useJakartaEe" to "true"
        )
    )
}

tasks.register("generateCodeFromOpenApiSpecs") {
    dependsOn("generateApiClient", "generateServerApi", "generateDomainEventsDTO")
}

// JOOQ Codegen

tasks.withType<KotlinCompile> {
    dependsOn("generateJooqClasses")
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.register("generateJooqClasses") {
    doLast {
        val schemaPath = "$rootDir/src/main/resources/db/schema.sql"

        val containerInstance = PostgreSQLContainer<Nothing>("postgres:16.3-alpine")
            .apply {
                withCopyToContainer(MountableFile.forHostPath(schemaPath), "/docker-entrypoint-initdb.d/init.sql")
                start()
            }

        Configuration()
            .withLogging(Logging.TRACE)
            .withJdbc(
                Jdbc()
                    .withDriver("org.postgresql.Driver")
                    .withUrl(containerInstance.jdbcUrl)
                    .withUser(containerInstance.username)
                    .withPassword(containerInstance.password)
            )
            .withGenerator(
                Generator()
                    .withDatabase(Database().withInputSchema("public"))
                    .withTarget(
                        Target()
                            .withPackageName("org.jooq.generated")
                            .withDirectory("${buildDir}/generated/jooq")
                    )
                    .withGenerate(
                        Generate()
                            .withNullableAnnotation(true)
                            .withNullableAnnotationType("jakarta.annotation.Nullable")
                    )
            ).also(GenerationTool::generate)

        containerInstance.stop()
    }
}