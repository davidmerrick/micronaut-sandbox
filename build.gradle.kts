repositories {
    mavenCentral()
    maven(url = "https://jcenter.bintray.com")
}

plugins {
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("jp.classmethod.aws.lambda") version "0.39"
    kotlin("jvm") version "1.3.50"
    kotlin("kapt") version "1.3.50"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.3.50"
    application
}

// Warn: This configuration has poor `file watch` performance on macOS
val developmentOnly: Configuration by configurations.creating

group = "com.merricklabs"
val mainClass = "io.micronaut.function.executor.FunctionApplication"

application {
    mainClassName = mainClass
    applicationDefaultJvmArgs = listOf("")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.50")
    implementation("io.micronaut:micronaut-runtime:1.3.4")
    implementation("io.micronaut:micronaut-function-aws:1.3.4")
    implementation("javax.annotation:javax.annotation-api")
    kapt(platform("io.micronaut:micronaut-bom:1.3.4"))
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")
    kaptTest(platform("io.micronaut:micronaut-bom:1.3.4"))
    kaptTest("io.micronaut:micronaut-inject-java:1.3.4")
    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2:1.0.0")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.9.1")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")
    testImplementation(platform("io.micronaut:micronaut-bom:1.3.4"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.8")
    testImplementation("io.micronaut.test:micronaut-test-kotlintest")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testImplementation("io.micronaut:micronaut-function-client")
    testImplementation("io.micronaut:micronaut-http-client")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.8")
    testRuntimeOnly("io.micronaut:micronaut-http-server-netty")
    testRuntimeOnly("io.micronaut:micronaut-function-web")
}

allOpen {
	annotation("io.micronaut.aop.Around")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
        }
    }

    test {
        useJUnitPlatform()
    }

    withType<JavaExec> {
        classpath += developmentOnly
        jvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")
    }

    shadowJar {
        manifest {
            attributes["Main-Class"] = mainClass
        }
        mergeServiceFiles()
    }
}