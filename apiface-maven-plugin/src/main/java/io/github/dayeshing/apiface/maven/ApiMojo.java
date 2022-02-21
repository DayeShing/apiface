package io.github.dayeshing.apiface.maven;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.dayeshing.apiface.conver.ToBean;
import io.github.dayeshing.apiface.core.api.meta.Author;
import io.github.dayeshing.apiface.core.api.resolver.ApiResolver;
import io.github.dayeshing.apiface.core.api.resolver.ApiResolverImpl;
import io.github.dayeshing.apiface.core.api.resolver.GetFieldGroupResolver;
import io.github.dayeshing.apiface.core.comment.impl.ClassMark;
import io.github.dayeshing.apiface.core.resolver.tag.AuthorResolver;
import io.github.dayeshing.apiface.core.swagger.v2.Swagger2;
import io.github.dayeshing.apiface.core.util.GZIPUtil;
import io.github.dayeshing.apiface.core.util.ObjectUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Description: <p> TODO </p>
 * @ClassName: ApiMojo
 * @ClassAllName: io.github.dayeshing.apiface.maven.ApiMojo
 * @Author： Daye Shing
 */
@Mojo(
        name = ApiMojo.MOJO,
        requiresDependencyResolution = ResolutionScope.COMPILE,
        threadSafe = true
)
@Execute(phase = LifecyclePhase.PACKAGE)
public class ApiMojo extends AbstractFaceMojo {

    public static final String MOJO = "api";

    @Parameter(
            property = "artifactId",
            defaultValue = ""
    )
    protected String artifactId;

    private ApiResolver resolver = null;

    public void loadMark(byte[] bytes) {
        try{
            if(gzip){
                resolver.resolver(ToBean.toBeans(GZIPUtil.uncompress(bytes)));
            }else {
                resolver.resolver(JSON.parseArray(new String(bytes), ClassMark.class));
            }
        }catch(Exception e){
            getLog().info(String.format("----------->> 解压文档失败: %s <<-----------", e.getMessage()));
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
                getLog().info(String.format("----------->> 正在加载文件: %s【%s】 <<-----------", url.getPath(), protocol));
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
    protected byte[] process() throws MojoExecutionException {
        resolver = new ApiResolverImpl(classLoader);
        loadMark(classLoader, String.format("%s/%s",DIRECTORY, getFilename(DocumentMojo.MOJO)));
        JSONObject transform = new Swagger2((GetFieldGroupResolver) resolver).transform(resolver.getActions(), resolver.getFields());
        return info(transform).toJSONString().getBytes();
    }

    @Override
    protected File dist() {
        return new File(project.getBuild().getDirectory(), getFilename(project.getBuild().getFinalName()));
    }

    protected JSONObject info(JSONObject transform) {
        transform.put("swagger", owner);
        transform.put("apiface", "1");
        transform.put("host", host);
        transform.put("ignoreHost", ignoreHost);
        transform.put("basePath", basePath);

        JSONObject info = new JSONObject();
        JSONObject license = new JSONObject();
        license.put("name", this.license);
        license.put("url", licenseUrl);
        info.put("license", license);

        JSONObject contact = new JSONObject();
        Author author = new AuthorResolver().resolver(Arrays.asList(developer));
        contact.put("url", url);
        contact.put("name", author.getName());
        contact.put("email", author.getContact());
        info.put("contact", contact);

        info.put("description", description);
        info.put("version", version);
        info.put("title", title);
        info.put("termsOfService", url);

        transform.put("info", info);
        return transform;
    }

    @Override
    protected boolean isExecute() {
        if(ObjectUtil.isEmpty(artifactId)){
            return true;
        }
        return artifactId.contains(project.getArtifactId());
    }

    @Override
    protected String mojo() {
        return MOJO;
    }

    @Parameter(
            property = "host",
            defaultValue = "127.0.0.1:8080"
    )
    private String host;
    @Parameter(
            property = "basePath",
            defaultValue = "/app"
    )
    private String basePath;
    @Parameter(
            property = "owner",
            defaultValue = "apiface © 土豆联盟出品"
    )
    private String owner;
    @Parameter(
            property = "license",
            defaultValue = "9527"
    )
    private String license;
    @Parameter(
            property = "licenseUrl",
            defaultValue = "http://self.daysh.top"
    )
    private String licenseUrl;
    @Parameter(
            property = "url",
            defaultValue = "http://self.daysh.top"
    )
    private String url;
    @Parameter(
            property = "developer",
            defaultValue = "Daye Shing|xuandeyu14@gmail.com"
    )
    private String developer;
    @Parameter(
            property = "description",
            defaultValue = "apiface让您的接口文档更丰富、更优雅"
    )
    private String description;
    @Parameter(
            property = "version",
            defaultValue = "1.0.0"
    )
    private String version;
    @Parameter(
            property = "title",
            defaultValue = "接口文档 APIs"
    )
    private String title;

    @Parameter(
            property = "ignoreHost",
            defaultValue = "false"
    )
    protected boolean ignoreHost;
}
