plugins {
    application
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "pl.jakubpradzynski"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.apache.spark:spark-sql_2.12:3.3.0")
    implementation("org.jetbrains.kotlinx.spark:kotlin-spark-api_3.3.0_2.12:1.2.3")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.google.cloud.spark:spark-bigquery_2.12:0.28.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.5")
    testImplementation("io.kotest:kotest-assertions-core:5.5.5")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("pl.jakubpradzynski.measurements.MeasurementsPerCityCounterJobKt")
}

tasks.shadowJar {
    isZip64 = true
}