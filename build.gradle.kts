plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    implementation("com.theokanning.openai-gpt3-java:service:0.14.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}