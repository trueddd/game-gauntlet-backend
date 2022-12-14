plugins {
    application
    kotlin("jvm") version Versions.Kotlin
    id("io.ktor.plugin") version Versions.Ktor
    id("org.jetbrains.kotlin.plugin.serialization") version Versions.Kotlin
    id("com.google.devtools.ksp") version Versions.KotlinKsp
}

group = Config.PackageName
version = Config.Version

application {
    mainClass.set("com.github.trueddd.ApplicationKt")
}

kotlin.sourceSets.main {
    kotlin.srcDirs(file("$buildDir/generated/ksp/main/kotlin"))
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":di"))
    ksp(project(":di"))
    implementation(Dependency.Logging)
    implementation(Dependency.Ktor.Server.Core)
    implementation(Dependency.Ktor.Server.Netty)
    implementation(Dependency.Ktor.Server.WebSockets)
    implementation(Dependency.Ktor.Server.ContentNegotiation)
    implementation(Dependency.Ktor.Server.CallLogging)
    implementation(Dependency.Ktor.Server.Cors)
    implementation(Dependency.Ktor.Server.DefaultHeaders)
    implementation(Dependency.Ktor.Server.ConditionalHeaders)
    implementation(Dependency.Ktor.Server.Auth)
    implementation(Dependency.Ktor.Server.AuthJwt)
    implementation(Dependency.Ktor.Serialization)
    implementation(Dependency.Ktor.Server.HtmlBuilder)
    implementation(Dependency.HtmlJvm)
    implementation(Dependency.CssJvm)
    ksp(Dependency.Koin.Compiler)
    testImplementation(Dependency.Ktor.Server.Tests)
    testImplementation(Dependency.Tests.Junit)
    testImplementation(Dependency.Tests.JupiterApi)
    testImplementation(Dependency.Tests.JupiterEngine)
}
