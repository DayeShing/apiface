/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.maven
 * @Author： Daye Shing
 * @CreateTime： 2020-08-13 22:17
 * @Description: <p>  </p>
 */
package com.daysh.apiface.maven;

import com.daysh.apiface.core.util.ObjectUtil;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @Description: <p> apiface 抽象mojo </p>
 * @ClassName: AbstractFaceMojo
 * @ClassAllName: com.daysh.apiface.maven.AbstractFaceMojo
 * @Author： Daye Shing
 * @Date： 2020-08-13 22:17
 */
public abstract class AbstractFaceMojo extends AbstractMojo {
    public static final String SUFFIX = ".json";
    public static final String DIRECTORY = "apiface";
    @Parameter(
            defaultValue = "${project}",
            readonly = true,
            required = true
    )
    protected MavenProject project;

    @Parameter(
            property = "sourseDirectory",
            defaultValue = "${project.build.sourceDirectory}"
    )
    protected File sourseDirectory;

    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.outputDirectory}"
    )
    protected File outputDirectory;

    protected ClassLoader projectClassLoader;
    protected static final Set<String> MODULE = new HashSet<>();

    protected abstract byte[] process() throws MojoExecutionException;

    protected abstract File dist();

    protected boolean isExecute(){
        return true;
    }

    protected abstract String mojo();

    protected String module(){
        return String.format("%s.%s",mojo(),project.getArtifactId());
    }

    public void execute() throws MojoExecutionException {
        // 执行重复
        if(ObjectUtil.equals("pom",project.getPackaging()) || !isExecute() || MODULE.contains(module())){
            return;
        }
        MODULE.add(module());
        try {
            before();
            ObjectUtil.write(dist(),process());
            after();
        }catch (Exception e){
            getLog().info(String.format("----------->> 构建 %s 模块的%s失败 <<-----------", project.getArtifactId(),mojo()));
            e.printStackTrace();
        }
    }

    protected boolean before(){
        getLog().info(String.format("----------->> 正在构建 %s 模块的%s <<-----------", project.getArtifactId(),mojo()));
        return true;
    }

    protected boolean after(){
        getLog().info(String.format("----------->> 成功构建 %s 模块的%s <<-----------", project.getArtifactId(),mojo()));
        return true;
    }

    protected String getFilename(String filename){
        return filename + SUFFIX;
    }

    protected List<String> getCompileClasspathElements(MavenProject p)  {
        try {
            return (List)(p.getCompileClasspathElements() == null ? Collections.emptyList() : new LinkedList(p.getCompileClasspathElements()));
        } catch (DependencyResolutionRequiredException e) {
            return Collections.emptyList();
        }
    }

    protected ClassLoader getProjectClassLoader() throws MojoExecutionException {
        if (this.projectClassLoader == null) {
            List classPath = this.getCompileClasspathElements(this.project);
            List<URL> urls = new ArrayList(classPath.size());
            Iterator i$ = classPath.iterator();
            while(i$.hasNext()) {
                String filename = (String)i$.next();
                try {
                    urls.add((new File(filename)).toURL());
                } catch (MalformedURLException var6) {
                    throw new MojoExecutionException("MalformedURLException: " + var6.getMessage(), var6);
                }
            }
            this.projectClassLoader = new URLClassLoader((URL[])urls.toArray(new URL[urls.size()]), (ClassLoader)null);
        }
        return this.projectClassLoader;
    }

}
