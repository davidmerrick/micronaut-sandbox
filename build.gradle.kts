repositories {
    mavenCentral()
    maven(url = "https://jcenter.bintray.com")
}

plugins {
    id("com.github.johnrengelman.shadow") version "5.2.0"
    kotlin("jvm") version "1.3.50"
    kotlin("kapt") version "1.3.50"
    application
}

group = "com.merricklabs.quarantinebot"

val micronautTestVersion by extra("1.1.5")
val micronautVersion by extra("1.3.4")
val jacksonVersion by extra("2.9.10")

application {
    mainClassName = "com.merricklabs.quarantinebot.Application"
    applicationDefaultJvmArgs = emptyList()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    implementation("io.micronaut:micronaut-runtime:$micronautVersion")
    implementation("io.micronaut.aws:micronaut-function-aws-api-proxy:$micronautVersion") {
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-afterburner")
    }
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("io.github.microutils:kotlin-logging:1.7.2")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.9.1")
    implementation("io.micronaut:micronaut-http-client:$micronautVersion")

    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")

    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2:1.0.0")

    // Test

    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kaptTest("io.micronaut:micronaut-inject-java:$micronautVersion")

    testImplementation("io.micronaut:micronaut-function-client:$micronautVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testImplementation("io.micronaut.test:micronaut-test-spock:$micronautTestVersion")
    testImplementation("io.micronaut.test:micronaut-test-kotlintest:$micronautTestVersion")
    testImplementation("io.micronaut.test:micronaut-test-junit5:$micronautTestVersion")
    testRuntimeOnly("io.micronaut:micronaut-http-server-netty:$micronautVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.spekframework.spek2:spek-runner-junit5:2.0.8")
    testImplementation("io.micronaut:micronaut-function-web:$micronautVersion")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
            javaParameters = true
        }
    }

    test {
        useJUnitPlatform()
    }

    shadowJar {
        archiveBaseName.set("quarantinebot")
        archiveClassifier.set("")
        archiveVersion.set("")
        mergeServiceFiles()
        transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer::class.java)
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
    }
}