import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.create

fun DependencyHandlerScope.implementation(dependency: Dependency) {
    add("implementation", create(dependency.notation))
}

fun DependencyHandler.testImplementation(dependency: Dependency) {
    add("testImplementation", create(dependency.notation))
}

sealed class Dependency(
    open val notation: String,
) {

    sealed class Ktor(notation: String) : Dependency(notation) {
        object Serialization : Ktor("io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.Ktor}")
        sealed class Server(notation: String) : Ktor(notation) {
            object Core : Server("io.ktor:ktor-server-core-jvm:${Versions.Ktor}")
            object Netty : Server("io.ktor:ktor-server-netty-jvm:${Versions.Ktor}")
            object WebSockets : Server("io.ktor:ktor-server-websockets-jvm:${Versions.Ktor}")
            object ContentNegotiation : Server("io.ktor:ktor-server-content-negotiation-jvm:${Versions.Ktor}")
            object HtmlBuilder : Server("io.ktor:ktor-server-html-builder-jvm:${Versions.Ktor}")
            object CallLogging : Server("io.ktor:ktor-server-call-logging-jvm:${Versions.Ktor}")
            object DefaultHeaders : Server("io.ktor:ktor-server-default-headers-jvm:${Versions.Ktor}")
            object ConditionalHeaders : Server("io.ktor:ktor-server-conditional-headers-jvm:${Versions.Ktor}")
            object Cors : Server("io.ktor:ktor-server-cors-jvm:${Versions.Ktor}")
            object Auth : Server("io.ktor:ktor-server-auth-jvm:${Versions.Ktor}")
            object AuthJwt : Server("io.ktor:ktor-server-auth-jwt-jvm:${Versions.Ktor}")
            object Tests : Server("io.ktor:ktor-server-tests-jvm:${Versions.Ktor}")
        }
    }

    object Logging : Dependency("ch.qos.logback:logback-classic:${Versions.Logback}")

    object CssJvm : Dependency("org.jetbrains:kotlin-css-jvm:1.0.0-pre.129-kotlin-1.4.20")
    object HtmlJvm : Dependency("org.jetbrains.kotlinx:kotlinx-html-jvm:${Versions.HtmlJvm}")

    object Junit : Dependency("org.jetbrains.kotlin:kotlin-test-junit:${Versions.Kotlin}")

    sealed class Koin(notation: String) : Dependency(notation) {
        object Core : Koin("io.insert-koin:koin-core:${Versions.Koin}")
    }

    sealed class Exposed(notation: String) : Dependency(notation) {
        object Core : Exposed("org.jetbrains.exposed:exposed-core:${Versions.Exposed}")
        object Dao : Exposed("org.jetbrains.exposed:exposed-dao:${Versions.Exposed}")
        object Jdbc : Exposed("org.jetbrains.exposed:exposed-jdbc:${Versions.Exposed}")
        object JavaTime : Exposed("org.jetbrains.exposed:exposed-java-time:${Versions.Exposed}")
    }

    object H2 : Dependency("com.h2database:h2:${Versions.H2}")
}