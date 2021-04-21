package io.github.dayeshing.apiface.gradle;

import com.alibaba.fastjson.JSON;
import io.github.dayeshing.apiface.core.util.ObjectUtil;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.ArtifactHandler;
import org.gradle.api.artifacts.dsl.ComponentModuleMetadataHandler;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.resources.ResourceHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * ApiPlugin的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @date 2021-03-04 19:19
 * @since 1.0
 */
public class ApifacePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(JavaPlugin.class);
        createTask(project, "docTask", DocTask.class, project.getTasks().getByName(JavaPlugin.COMPILE_JAVA_TASK_NAME));
        createTask(project, "apiTask", ApiTask.class, project.getTasks().getByName("build"));
        project.getExtensions().create("apiface", ApiExtension.class);
    }

    private <T extends Task> T createTask(Project project, String taskName, Class<T> taskClass, Task task) {
        T t = project.getTasks().create(taskName, taskClass);
        t.setGroup("apiface");
        task.finalizedBy(t);
        return t;
    }

}
