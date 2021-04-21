package io.github.dayeshing.apiface.gradle;
import java.io.FileFilter;
import	java.util.Arrays;
import	java.io.File;

import com.alibaba.fastjson.JSON;
import io.github.dayeshing.apiface.core.util.ObjectUtil;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.artifacts.component.ComponentIdentifier;
import org.gradle.api.artifacts.result.ArtifactResult;
import org.gradle.api.artifacts.result.ComponentArtifactsResult;
import org.gradle.api.component.Artifact;
import org.gradle.api.component.Component;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.resources.ResourceHandler;
import org.gradle.api.resources.TextResourceFactory;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskAction;
import org.gradle.jvm.JvmLibrary;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

//import org.gradle.api.internal.artifacts.result.DefaultResolvedArtifactResult;
//import org.gradle.language.base.artifact.SourcesArtifact;

/**
 * AbstractFaceTask的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @date 2021-04-21 05:46
 * @since 1.0
 */
public abstract class AbstractFaceTask extends DefaultTask {

    public static final String SUFFIX = ".json";
    public static final String DIRECTORY = "apiface";
    
    protected ClassLoader classLoader;

    protected Project project;

    protected ApiExtension apiExtension;

    protected File sourseDirectory;

    protected File outputDirectory;

    protected abstract byte[] process();

    protected abstract File dist();

    protected boolean isExecute(){
        return true;
    }

    protected abstract String mojo();

    @TaskAction
    public void action() {
        project = getProject();
        apiExtension = project.getExtensions().getByType(ApiExtension.class);
        sourseDirectory = new File(String.format("%s/src/main/java",project.getProjectDir()));
        outputDirectory = new File(String.format("%s/classes/java/main",project.getBuildDir()));
        try {
            classLoader = getClassLoader();
            before();
            byte[] process = process();
            if(process != null){
                ObjectUtil.write(dist(),process);
            }
            after();
        }catch (Exception e){
            getLog(String.format("----------->> 构建 %s 模块的%s失败 <<-----------", project.getName(),mojo()));
            e.printStackTrace();
        }
        apiExtension = project.getExtensions().getByType(ApiExtension.class);

    }

    protected void getLog(String log){
        System.out.println(log);
    }

    protected boolean before(){
        getLog(String.format("----------->> 正在构建 %s 模块的%s <<-----------", project.getName(),mojo()));
        return true;
    }

    protected boolean after(){
        getLog(String.format("----------->> 成功构建 %s 模块的%s <<-----------", project.getName(),mojo()));
        return true;
    }

    protected String getFilename(String filename){
        return filename + SUFFIX;
    }

//    protected List<String> getCompileClasspathElements(Project p)  {
////        Configuration compileConfiguration = project.getConfigurations().getByName("compile");
////        List<ComponentIdentifier> binaryDependencies = new ArrayList<ComponentIdentifier>();
////        TreeMap<String, Project> allModules = this.getAllModule(project.getRootProject());
////        Set<ResolvedArtifact> resolvedArtifacts = compileConfiguration.getResolvedConfiguration().getResolvedArtifacts();
////        for (ResolvedArtifact resolvedArtifact : resolvedArtifacts) {
////            String displayName = resolvedArtifact.getId().getComponentIdentifier().getDisplayName();
////            CustomArtifact moduleArtifact = null;
////            boolean selfModule = displayName.startsWith("project :");
////            if (selfModule) {
////                ResolvedModuleVersion version = resolvedArtifact.getModuleVersion();
////                moduleArtifact = CustomArtifact.builder().setGroup(version.getId().getGroup())
////                        .setArtifactId(version.getId().getName())
////                        .setVersion(version.getId().getVersion());
////                // add local source
////                String artifactName = moduleArtifact.getGroup() + ":" + moduleArtifact.getArtifactId();
////                addModuleSourceTree(javaDocBuilder, allModules, artifactName);
////
////            }
////            CustomArtifact artifact = selfModule ? moduleArtifact : CustomArtifact.builder(displayName);
////            if (ArtifactFilterUtil.ignoreArtifact(artifact) || ArtifactFilterUtil.ignoreSpringBootArtifactById(artifact)) {
////                continue;
////            }
////            String artifactName = artifact.getGroup() + ":" + artifact.getArtifactId();
////            if (RegexUtil.isMatches(excludes, artifactName)) {
////                continue;
////            }
////            if (RegexUtil.isMatches(includes, artifactName)) {
////                if (selfModule) {
////                    addModuleSourceTree(javaDocBuilder, allModules, displayName);
////                    continue;
////                }
////                binaryDependencies.add(resolvedArtifact.getId().getComponentIdentifier());
////                continue;
////            }
////            if (includes.size() < 1 && !selfModule) {
////                binaryDependencies.add(resolvedArtifact.getId().getComponentIdentifier());
////            }
////        }
////        dependencies.createArtifactResolutionQuery()
//////                .forComponents(componentIds)
//////                .withArtifacts(JvmLibrary, SourcesArtifact)
//////                .execute()
//        Set<ComponentArtifactsResult> resolvedComponents = project.getDependencies().createArtifactResolutionQuery()
//                .forComponents(new ArrayList<ComponentIdentifier>())
//                .withArtifacts(Component.class, Artifact.class)
//                .execute()
//                .getResolvedComponents();
//
//        for (ComponentArtifactsResult artifactResult : resolvedComponents) {
//            Set<ArtifactResult> artifacts = artifactResult.getArtifacts(Artifact.class);
//            for (ArtifactResult artifact : artifacts) {
//                System.err.println(JSON.toJSONString(artifact));
//            }
//
////            for (ArtifactResult sourcesResult : artifactResult.getArtifacts(SourcesArtifact.class)) {
////                if (sourcesResult instanceof DefaultResolvedArtifactResult) {
////                    this.loadSourcesDependency(javaDocBuilder, (DefaultResolvedArtifactResult) sourcesResult);
////                }
////            }
//        }
//        Configuration compileConfiguration = p.getConfigurations().getByName("compile");
//        System.err.println(compileConfiguration.getName());
//        Set<ResolvedArtifact> resolvedArtifacts = compileConfiguration.getResolvedConfiguration().getResolvedArtifacts();
//        for (ResolvedArtifact resolvedArtifact : resolvedArtifacts) {
//            System.err.println(resolvedArtifact.getFile().getAbsolutePath());
//        }
//        try {
////            if(p.getCompileClasspathElements() == null){
//                return Collections.emptyList();
////            }
////            return new LinkedList<String>(p.getCompileClasspathElements());
//        } catch (Exception e) {
//            return Collections.emptyList();
//        }
//    }

    protected List<String> jar(Project p,String dir){
        File file = new File(p.getBuildDir(),dir);
        List<String> ret = new ArrayList<String>();
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && pathname.getName().endsWith("jar");
            }
        });
        for (File f : files) {
            ret.add(f.getAbsolutePath());
        }
        ret.add(outputDirectory.getAbsolutePath());
        return ret;
    }

    protected List<String> getCompileClasspathElements(Project p)  {
        return jar(p, "jars");
    }
    protected ClassLoader getClassLoader() {
        List<String> classPath = getCompileClasspathElements(project);
        List<URL> urls = new ArrayList<URL>(classPath.size());
        for (String jar : classPath) {
            try {
                urls.add((new File(jar)).toURL());
            } catch (Exception var6) {
                getLog(String.format("----------->> 加载包 %s 失败 <<-----------", jar));
            }
        }
        URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
        Thread.currentThread().setContextClassLoader(loader);
        return loader;
    }


    private TreeMap<String, Project> getAllModule(Project rootProject) {
        TreeMap<String, Project> result = new TreeMap<String, Project>();
        if (rootProject == null) {
            return result;
        }
        if (rootProject.getDepth() != 0) {
            result.put(rootProject.getGroup() + ":" + rootProject.getName(), rootProject);
        }
        if (rootProject.getChildProjects().isEmpty()) {
            return result;
        }
        Map<String, Project> map = rootProject.getChildProjects();
        for (Map.Entry<String, Project> entry : map.entrySet()) {
            TreeMap<String, Project> module = getAllModule(entry.getValue());
            result.putAll(module);
        }
        return result;
    }


}
