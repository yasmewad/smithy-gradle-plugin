description = "Simple Smithy Java server code generation example"

plugins {
    id("software.amazon.smithy.gradle.smithy-java-codegen").version("1.4.0")
}

group = "software.amazon.smithy"
version = "9.9.9"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("software.amazon.smithy:smithy-model:[1.0, 2.0[")
    smithyBuild("software.amazon.smithy.java:plugins:[1.0, 2.0[")
}
