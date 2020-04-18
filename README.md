# quarantinebot

Serverless Slack bot which tells you how long you've been quarantined in units of facemasks.

* Kotlin
* Built with superfast Micronaut framework
* Runs on AWS Lambda
* Includes Terraform configs for deploying infrastructure

![screenshot](img/screenshot.png)

# Running tests locally

1. Do a `./gradlew build` to generate kapt annotations
2. Run tests as usual in IntelliJ
