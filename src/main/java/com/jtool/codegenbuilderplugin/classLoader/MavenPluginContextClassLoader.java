package com.jtool.codegenbuilderplugin.classLoader;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * Created by jialechan on 15/6/24.
 */
public class MavenPluginContextClassLoader implements ClassLoaderInterface {

    private URLClassLoader newLoader;

    public MavenPluginContextClassLoader(MavenProject project) throws DependencyResolutionRequiredException {
        List runtimeClasspathElements = project.getRuntimeClasspathElements();
        URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
        for (int i = 0; i < runtimeClasspathElements.size(); i++) {
            String element = (String) runtimeClasspathElements.get(i);
            try {
                runtimeUrls[i] = new File(element).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        newLoader = new URLClassLoader(runtimeUrls,
                Thread.currentThread().getContextClassLoader());
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return newLoader.loadClass(name);
    }

}
