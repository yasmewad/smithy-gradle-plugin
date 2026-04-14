description = "Configures a Java project for Smithy Java code generation, " +
        "using Smithy's java-codegen plugin to generate server and client stubs."

plugins {
    id("smithy-gradle-plugin.plugin-conventions")
}

gradlePlugin {
    plugins {
        create("smithy-java-codegen-plugin") {
            id = "${group}.smithy-java-codegen"
            displayName = "Smithy Gradle Java Codegen plugin."
            description = project.description
            implementationClass = "software.amazon.smithy.gradle.SmithyJavaCodegenPlugin"
            tags.addAll("smithy", "api", "building", "codegen")
        }
    }
}

dependencies {
    implementation(project(":smithy-base"))
}
