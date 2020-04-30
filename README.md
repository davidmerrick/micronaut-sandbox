# quarantinebot

Serverless Slack bot which tells you how long you've been quarantined in units of facemasks.

* Written in Kotlin
* Built with superfast [Micronaut](https://micronaut.io/) framework
* Runs on AWS Lambda
* Includes Terraform configs for deploying infrastructure

![screenshot](img/screenshot.png)

# Running tests locally

1. Do a `./gradlew build` to generate kapt annotations
2. Run tests as usual in IntelliJ

# References

* [Micronaut GraalVM tutorial](https://guides.micronaut.io/micronaut-function-graalvm-aws-lambda-gateway/guide/index.html)
* [Netty on GraalVM](https://medium.com/graalvm/instant-netty-startup-using-graalvm-native-image-generation-ed6f14ff7692)