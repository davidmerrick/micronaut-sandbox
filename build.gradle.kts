repositories {
    mavenCentral()
    jcenter()
}

plugins {
    id("com.github.johnrengelman.shadow") version "5.2.0"
    kotlin("jvm") version "1.3.72"
    kotlin("kapt") version "1.3.72"
    kotlin("plugin.allopen") version "1.3.72"
    id("com.hpe.kraal") version "0.0.15"
    application
}

// Compiler plugin which makes classes with the following
// annotations open
allOpen {
    annotations(
            "io.micronaut.aop.Around",
            "io.micronaut.http.annotation.Controller",
            "javax.inject.Singleton"
    )
}

group = "io.github.davidmerrick.quarantinebot"

application {
    mainClassName = "io.github.davidmerrick.quarantinebot.Application"
}

dependencies {
    val micronautVersion by extra("1.3.4")

    kapt(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kapt("io.micronaut:micronaut-graal")
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")

    compileOnly("org.graalvm.nativeimage:svm:20.0.0")

    implementation(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.aws:micronaut-function-aws-api-proxy") {
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-afterburner")
    }
    implementation("io.micronaut.aws:micronaut-function-aws-custom-runtime") {
        exclude(group = "com.fasterxml.jackson.module", module = "jackson-module-afterburner")
    }
    implementation("io.micronaut:micronaut-inject")
    implementation("io.micronaut:micronaut-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.github.microutils:kotlin-logging:1.7.2")
    implementation("org.slf4j:slf4j-simple:1.8.0-beta4")
    implementation("io.micronaut:micronaut-http-server-netty")
    implementation("io.github.davidmerrick.slakson:slakson:2.0.1")

    // Test

    kaptTest(platform("io.micronaut:micronaut-bom:$micronautVersion"))
    kaptTest("io.micronaut:micronaut-inject-java")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
    testImplementation("io.micronaut.test:micronaut-test-spock")
    testImplementation("io.micronaut.test:micronaut-test-kotlintest")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.spekframework.spek2:spek-runner-junit5:2.0.8")
    testImplementation("io.mockk:mockk:1.10.0")
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

    named<JavaExec>("run") {
        doFirst {
            jvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")
        }
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