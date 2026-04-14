package software.amazon.smithy.gradle;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

public class GeneratesServerCodeTest {

    @Test
    public void generatesServerCode() {
        Utils.withCopy("java-codegen-plugin/simple-server", buildDir -> {
            BuildResult result = GradleRunner.create()
                    .forwardOutput()
                    .withProjectDir(buildDir)
                    .withArguments("clean", "build", "--stacktrace")
                    .build();

            Utils.assertSmithyBuildTaskRan(result);
            Utils.assertArtifactsCreated(buildDir,
                    "build/smithyprojections/simple-server/source/java-codegen/java",
                    "build/smithyprojections/simple-server/source/java-codegen/resources");
        });
    }
}
