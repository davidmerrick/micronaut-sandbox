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

application {
    mainClassName = "com.merricklabs.quarantinebot.Application"
    applicationDefaultJvmArgs = listOf("")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    implementation("io.micronaut:micronaut-runtime:1.3.4")
    implementation("io.micronaut.aws:micronaut-function-aws-api-proxy:1.3.4")
    kapt(platform("io.micronaut:micronaut-bom:1.3.4"))
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut:micronaut-validation")
    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2:1.0.0")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.9.1")
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

    shadowJar {
        archiveBaseName.set("quarantinebot")
        archiveClassifier.set("")
        archiveVersion.set("")
        mergeServiceFiles()
        transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer::class.java)
    }

    withType<Jar> {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
    }
}