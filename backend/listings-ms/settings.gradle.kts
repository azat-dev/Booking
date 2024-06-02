pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}


rootProject.name = "listings-ms"


include(":shared")
project(":shared").projectDir = File("../shared")

include(":apiclient")
project(":apiclient").projectDir = File("./build/generated/client")