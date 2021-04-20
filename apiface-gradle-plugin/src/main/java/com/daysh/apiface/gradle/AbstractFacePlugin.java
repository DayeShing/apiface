package com.daysh.apiface.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.File;


/**
 * AbstractFacePlugin的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @date 2021-03-04 19:16
 * @since 1.0
 */
public abstract class AbstractFacePlugin implements Plugin<Project> {

    public static final String SUFFIX = ".json";
    public static final String DIRECTORY = "apiface";

    protected ClassLoader projectClassLoader;

    protected Project project;
    protected abstract byte[] process();

    protected abstract File dist();

    protected boolean isExecute(){
        return true;
    }

    protected abstract String mojo();

    @Override
    public void apply(Project project) {
        // 执行重复
        if(!isExecute()){
            return;
        }
        try {
            before();
            process();
//            ObjectUtil.write(dist(),process());
            after();
        }catch (Exception e){
            getLog().info(String.format("----------->> 构建文档的%s失败 <<-----------",mojo()));
            e.printStackTrace();
        }
    }

    protected boolean before(){
        getLog().info(String.format("----------->> 正在构建文档的%s <<-----------", mojo()));
        return true;
    }

    protected boolean after(){
        getLog().info(String.format("----------->> 成功构建文档的%s <<-----------", mojo()));
        return true;
    }

    protected String getFilename(String filename){
        return filename + SUFFIX;
    }

    protected Logger getLog(){
        return project.getLogger();
    }


}
