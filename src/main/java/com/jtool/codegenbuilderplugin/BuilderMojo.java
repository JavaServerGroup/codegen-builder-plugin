package com.jtool.codegenbuilderplugin;

import com.alibaba.fastjson.JSON;
import com.jtool.codegenbuilderplugin.classLoader.ClassLoaderInterface;
import com.jtool.codegenbuilderplugin.classLoader.MavenPluginContextClassLoader;
import com.jtool.codegenbuilderplugin.finder.FileFinder;
import com.jtool.codegenbuilderplugin.finder.MethodFinder;
import com.jtool.codegenbuilderplugin.generator.ApiGenerator;
import com.jtool.codegenbuilderplugin.generator.HtmlGenerator;
import com.jtool.codegenbuilderplugin.generator.PojoGenerator;
import com.jtool.codegenbuilderplugin.model.CodeGenModel;
import com.jtool.codegenbuilderplugin.model.LogicInfo;
import com.jtool.codegenbuilderplugin.parser.LogicInfoParser;
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
import java.util.Map;

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
    private Map<String, String> hosts;

    /**
     * @parameter
     * @required
     */
    private String projectName = "MyProject";

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

    /**
     * @parameter expression = "${project.basedir}/docSource/info.html"
     * @required
     * @readonly
     */
    private File infoHtmlFile;

    /**
     * @parameter expression = "${project.basedir}/docSource/changeLog.html"
     * @required
     * @readonly
     */
    private File changeLogHtmlFile;

    /**
     * @parameter expression = "${skipGenAndroidSDK}"
     * @parameter default-value = "false"
     */
    private boolean skipGenAndroidSDK;

    private ClassLoaderInterface classLoaderInterface;

    private String scanSource = "/src/main/java/";

    public void execute() throws MojoExecutionException, MojoFailureException {

        //初始化classesLoader
        initClassesLoader();

        //递归找出需要扫描的file
        List<File> files = FileFinder.findAllFileNeedToParse(this);

        //递归扫描scanBasePackage下的<logicInfo></logicInfo>内的内容
        List<LogicInfo> logicInfoList = LogicInfoParser.parseLogicInfoFromFiles(files);

        //遍历带有@CodeGenApi注解的方法
        List<Method> methodLists = MethodFinder.findAllCodeGenApiMethod(this, files);

        //遍历method集合，解析出可用的apiModel对象集合
        List<CodeGenModel> codeGenModelList = MethodParser.parseMethodToCodeGenModel(this, methodLists);

        this.getLog().debug(JSON.toJSONString(codeGenModelList));

        //生成html格式的文档。
        HtmlGenerator.genHtmlDoc(this, codeGenModelList, logicInfoList);

        //生成pojo
        try {
            PojoGenerator.genPojo(this, codeGenModelList);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //生成api
        try {
            if(!this.skipGenAndroidSDK) {
                ApiGenerator.genApi(this, codeGenModelList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            this.getLog().debug("生成api的时候发生错误");
            e.printStackTrace();
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

    public File getInfoHtmlFile() {
        return infoHtmlFile;
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

    public void setInfoHtmlFile(File infoHtmlFile) {
        this.infoHtmlFile = infoHtmlFile;
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

    public File getChangeLogHtmlFile() {
        return changeLogHtmlFile;
    }

    public void setChangeLogHtmlFile(File changeLogHtmlFile) {
        this.changeLogHtmlFile = changeLogHtmlFile;
    }

    public Map<String, String> getHosts() {
        return hosts;
    }

    public void setHosts(Map<String, String> hosts) {
        this.hosts = hosts;
    }
}
