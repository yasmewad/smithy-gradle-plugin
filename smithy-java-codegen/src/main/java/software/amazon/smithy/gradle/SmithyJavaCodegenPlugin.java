/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package software.amazon.smithy.gradle;

import java.nio.file.Path;
import javax.inject.Inject;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

/**
 * A {@link org.gradle.api.Plugin} that configures a project for Smithy Java code generation.
 */
public class SmithyJavaCodegenPlugin implements Plugin<Project> {
    private static final String JAVA_CODEGEN_PLUGIN_NAME = "java-codegen";

    private final Project project;

    @Inject
    public SmithyJavaCodegenPlugin(Project project) {
        this.project = project;
    }

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(JavaLibraryPlugin.class);
        project.getPlugins().apply(SmithyBasePlugin.class);

        SmithyExtension extension = project.getExtensions().getByType(SmithyExtension.class);

        // Add java-codegen output to main source set
        project.getExtensions().getByType(SourceSetContainer.class).all(sourceSet -> {
            if (SourceSet.isMain(sourceSet)) {
                // Add generated Java sources
                sourceSet.getJava().srcDir(project.provider(() ->
                        resolveCodegenPath(extension).resolve("java").toFile()));
                // Add generated resources (e.g., META-INF/services for SchemaIndex)
                sourceSet.getResources().srcDir(project.provider(() ->
                        resolveCodegenPath(extension).resolve("resources").toFile()))
                        .exclude("**/*.java");
            }
        });

        // Wire task dependencies
        project.getTasks().named("compileJava", task ->
                task.dependsOn(SmithyBasePlugin.SMITHY_BUILD_TASK_NAME));
        project.getTasks().named("processResources", task ->
                task.dependsOn(SmithyBasePlugin.SMITHY_BUILD_TASK_NAME));
    }

    private static Path resolveCodegenPath(SmithyExtension extension) {
        try {
            return extension.getPluginProjectionPath(
                    extension.getSourceProjection().get(), JAVA_CODEGEN_PLUGIN_NAME)
                    .get().toAbsolutePath();
        } catch (Exception e) {
            throw new GradleException(
                    "Could not resolve projection path for plugin '" + JAVA_CODEGEN_PLUGIN_NAME
                            + "'. Ensure smithy-build.json defines this plugin in the '"
                            + extension.getSourceProjection().get() + "' projection.", e);
        }
    }
}
