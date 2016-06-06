package com.jtool.codegenbuilderplugin.test;

import com.jtool.codegenbuilderplugin.BuilderMojo;
import com.jtool.codegenbuilderplugin.test.util.TestContextClassLoader;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

public class Tester {

    @Test
    public void testGen() throws MojoFailureException, MojoExecutionException, DependencyResolutionRequiredException {

        String baseUrl = this.getClass().getResource("/").getPath().replace("/target/test-classes/", "");

        BuilderMojo builderMojo = new BuilderMojo();
        TestContextClassLoader testContextClassLoader = new TestContextClassLoader();
        builderMojo.setClassLoaderInterface(testContextClassLoader);

        File baseDirFile = new File(baseUrl);
        builderMojo.setOutPath(baseDirFile.getAbsolutePath() + "/target/");
        builderMojo.setBasedir(baseDirFile);

        Map<String, String> hosts = new Hashtable<>();
        hosts.put("新加坡", "172.17.20.13");
        hosts.put("爱尔兰", "172.17.0.158");
        builderMojo.setHosts(hosts);

        builderMojo.setProjectName("codegen-builder-plugin");
        builderMojo.setScanSource("/src/test/java/");
        builderMojo.setScanBasePackage("com.jtool.codegenbuilderplugin");
        builderMojo.setInfoHtmlFile(new File(baseDirFile.getAbsolutePath() + "/docsource/info.html"));
        builderMojo.setChangeLogHtmlFile(new File(baseDirFile.getAbsolutePath() + "/docsource/changeLog.html"));
        builderMojo.execute();
    }
}
