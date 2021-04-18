/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.maven
 * @Author： Daye Shing
 * @CreateTime： 2020-08-13 22:17
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.maven;

import io.github.dayeshing.apiface.core.util.ObjectUtil;
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
 * @ClassAllName: io.github.dayeshing.apiface.maven.AbstractFaceMojo
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

    @Parameter(
            property = "gzip",
            defaultValue = "true"
    )
    protected boolean gzip;

    protected ClassLoader classLoader;

    protected abstract byte[] process() throws MojoExecutionException;

    protected abstract File dist();

    protected boolean isExecute(){
        return true;
    }

    protected abstract String mojo();

    protected String module(){
        return String.format("%s.%s",mojo(),project.getArtifactId());
    }

    @Override
    public void execute() throws MojoExecutionException {
        if(ObjectUtil.equals("pom",project.getPackaging())
                || !isExecute()
        ){
            return;
        }
        try {
            classLoader = getClassLoader();
            before();
            byte[] process = process();
            if(process != null){
                ObjectUtil.write(dist(),process);
            }
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
            if(p.getCompileClasspathElements() == null){
                return Collections.emptyList();
            }
            return new LinkedList<String>(p.getCompileClasspathElements());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    protected ClassLoader getClassLoader() {
        List<String> classPath = getCompileClasspathElements(this.project);
        List<URL> urls = new ArrayList<URL>(classPath.size());
        for (String jar : classPath) {
            try {
                urls.add((new File(jar)).toURL());
            } catch (Exception var6) {
                getLog().info(String.format("----------->> 加载包 %s 失败 <<-----------", jar));
            }
        }
        URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(loader);
        return loader;
    }

}
