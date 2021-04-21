package io.github.dayeshing.apiface.gradle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.dayeshing.apiface.conver.ToBean;
import io.github.dayeshing.apiface.core.api.meta.Author;
import io.github.dayeshing.apiface.core.api.resolver.ApiResolver;
import io.github.dayeshing.apiface.core.api.resolver.ApiResolverImpl;
import io.github.dayeshing.apiface.core.comment.impl.ClassMark;
import io.github.dayeshing.apiface.core.resolver.tag.AuthorResolver;
import io.github.dayeshing.apiface.core.swagger.v2.Swagger2;
import io.github.dayeshing.apiface.core.util.GZIPUtil;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ApiTask的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @date 2021-04-21 05:48
 * @since 1.0
 */
public class ApiTask extends AbstractFaceTask {

    public static final String MOJO = "api";
    
    private ApiResolver resolver = null;

    public void loadMark(byte[] bytes) {
        try{
            if(apiExtension.isGzip()){
                resolver.resolver(ToBean.toBeans(GZIPUtil.uncompress(bytes)));
            }else {
                resolver.resolver(JSON.parseArray(new String(bytes), ClassMark.class));
            }
        }catch(Exception e){
            getLog(String.format("----------->> 解压文档失败: %s <<-----------", e.getMessage()));
            e.printStackTrace();
        }
    }

    /**
     * 加载jar中的数据
     * @param loader |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @param pack |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @uri uri
     * @summary 摘要
     * @method post,get
     * @ignore
     * @exclude 排除参数1,排除参数2
     * @return void|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     */
    public void loadMark(ClassLoader loader, String pack) {
        Enumeration<URL> resources = null;
        try {
            resources = loader.getResources(pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resources != null) {
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String protocol = url.getProtocol();
                getLog(String.format("----------->> 正在加载文件: %s【%s】 <<-----------", url.getPath(), protocol));
                if ("file".equalsIgnoreCase(protocol)) {
                    try {
                        loadMark(ObjectUtil.read(new FileInputStream(new File(url.getFile()))));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if ("jar".equalsIgnoreCase(protocol)) {
                    JarFile jares = null;
                    try {
                        jares = ((JarURLConnection) url.openConnection()).getJarFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (jares != null) {
                        Enumeration<JarEntry> entries = jares.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry = entries.nextElement();
                            if (jarEntry != null) {
                                String className = jarEntry.getName();
                                if (className.startsWith(pack)) {
                                    try {
                                        loadMark(ObjectUtil.read(jares.getInputStream(jarEntry)));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected byte[] process() {
        resolver = new ApiResolverImpl(classLoader);
        loadMark(classLoader, String.format("%s/%s",DIRECTORY, getFilename(DocTask.MOJO)));
        try {
            Class clazz = Class.forName("com.daysh.apiface.controller.GraldleController", false, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        JSONObject transform = new Swagger2().transform(resolver.getActions(), resolver.getFields());
        return info(transform).toJSONString().getBytes();
    }

    @Override
    protected File dist() {
        return new File(new File(project.getBuildDir(),"libs"), getFilename(project.getName()));
    }

    protected JSONObject info(JSONObject transform) {
        transform.put("swagger", apiExtension.getOwner());
        transform.put("host", apiExtension.getHost());
        transform.put("basePath", apiExtension.getBasePath());

        JSONObject info = new JSONObject();
        JSONObject license = new JSONObject();
        license.put("name", apiExtension.getLicense());
        license.put("url", apiExtension.getLicenseUrl());
        info.put("license", license);

        JSONObject contact = new JSONObject();
        Author author = new AuthorResolver().resolver(Arrays.asList(apiExtension.getDeveloper()));
        contact.put("url", apiExtension.getUrl());
        contact.put("name", author.getName());
        contact.put("email", author.getContact());
        info.put("contact", contact);

        info.put("description", apiExtension.getDescription());
        info.put("version", apiExtension.getVersion());
        info.put("title", apiExtension.getTitle());
        info.put("termsOfService", apiExtension.getUrl());

        transform.put("info", info);
        return transform;
    }
    @Override
    protected String mojo() {
        return MOJO;
    }

//    @Override
//    protected boolean isExecute() {
//        return ObjectUtil.equals(artifactId, project.getArtifactId());
//    }
}
