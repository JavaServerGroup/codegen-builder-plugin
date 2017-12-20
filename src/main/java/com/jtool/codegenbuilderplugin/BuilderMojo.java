package com.jtool.codegenbuilderplugin;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenbuilderplugin.classLoader.ClassLoaderInterface;
import com.jtool.codegenbuilderplugin.classLoader.MavenPluginContextClassLoader;
import com.jtool.codegenbuilderplugin.finder.ExceptionFinder;
import com.jtool.codegenbuilderplugin.finder.FileFinder;
import com.jtool.codegenbuilderplugin.finder.MethodFinder;
import com.jtool.codegenbuilderplugin.generator.DocMdFormatGenerator;
import com.jtool.codegenbuilderplugin.model.CodeGenModel;
import com.jtool.codegenbuilderplugin.model.ExceptionModel;
import com.jtool.codegenbuilderplugin.parser.MethodParser;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @goal build
 * @requiresDependencyResolution runtime
 */
public class BuilderMojo extends AbstractMojo {

    /**
     * @parameter expression = "${project.basedir}"
     * @required
     * @readonly
     */
    private File basedir;

    /**
     * @parameter
     * @required
     */
    private String scanBasePackage;

    /**
     * @parameter
     */
    private String projectName = "CodeGenDoc";

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter default-value = "${project.build.directory}/"
     */
    private String outPath;

    private ClassLoaderInterface classLoaderInterface;

    private String scanSource = "/src/main/java/";

    public void execute() throws MojoExecutionException, MojoFailureException {

        //初始化classesLoader
        initClassesLoader();

        //递归找出需要扫描的file
        List<File> files = FileFinder.findAllFileNeedToParse(this);

        //遍历带有@CodeGenApi注解的方法
        List<Method> methodLists = MethodFinder.findAllCodeGenApiMethod(this, files);

        //遍历所有定义的Exception
        List<ExceptionModel> exceptionModels = ExceptionFinder.findAllCodeGenException(this, files);

        //检查是否有一样错误码的异常
        checkDuplicateExceptionCodeDefine(exceptionModels);

        //遍历method集合，解析出可用的apiModel对象集合
        List<CodeGenModel> codeGenModelList;
        try {
            codeGenModelList = MethodParser.parseMethodToCodeGenModel(this, methodLists);
        } catch (ClassNotFoundException | IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        this.getLog().debug(JSON.toJSONString(codeGenModelList));

        //生成md文件
        DocMdFormatGenerator.genMdDoc(this, codeGenModelList, exceptionModels);
    }

    private void checkDuplicateExceptionCodeDefine(List<ExceptionModel> exceptionModels) {
        if(exceptionModels.size() == 1) {
            return;
        }
        for(int i = 0; i < exceptionModels.size(); i++) {
            for(int j = i + 1; j < exceptionModels.size(); j++) {
                if( exceptionModels.get(i).getCode().equals(exceptionModels.get(j).getCode())) {
                    throw new RuntimeException("一个项目里面不应该定义两个异常是有相同错误码的");
                }
            }
        }
    }

    private void initClassesLoader() {
        if (classLoaderInterface == null) {
            try {
                classLoaderInterface = new MavenPluginContextClassLoader(project);
            } catch (DependencyResolutionRequiredException e) {
                e.printStackTrace();
            }
        }
    }

    public String getOutPath() {
        return outPath;
    }

    public String getScanBasePackage() {
        return scanBasePackage;
    }

    public void setScanBasePackage(String scanBasePackage) {
        this.scanBasePackage = scanBasePackage;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public ClassLoaderInterface getClassLoaderInterface() {
        return classLoaderInterface;
    }

    public void setClassLoaderInterface(ClassLoaderInterface classLoaderInterface) {
        this.classLoaderInterface = classLoaderInterface;
    }

    public File getBasedir() {
        return basedir;
    }

    public void setBasedir(File basedir) {
        this.basedir = basedir;
    }

    public String getScanSource() {
        return scanSource;
    }

    public void setScanSource(String scanSource) {
        this.scanSource = scanSource;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
