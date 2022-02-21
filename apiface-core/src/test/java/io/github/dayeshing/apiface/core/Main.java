
 /**
 * @BelongsProject： apiface
 * @BelongsPackage： PACKAGE_NAME
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 22:23
 * @Description: <p>  </p>
 */
 package io.github.dayeshing.apiface.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.github.dayeshing.apiface.core.api.ApiRule;
import io.github.dayeshing.apiface.core.api.resolver.ApiResolver;
import io.github.dayeshing.apiface.core.api.resolver.ApiResolverImpl;
import io.github.dayeshing.apiface.core.comment.impl.ClassMark;
import io.github.dayeshing.apiface.core.resolver.impl.ResolverImpl;
import io.github.dayeshing.apiface.core.swagger.v2.Swagger2;
import io.github.dayeshing.apiface.core.util.ImportUtil;
import io.github.dayeshing.apiface.core.util.ScanUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: io.github.dayeshing.apiface.core.Main
 * @ClassAllName: PACKAGE_NAME.io.github.dayeshing.apiface.core.Main
 * @Author： Daye Shing
 * @Date： 2020-08-15 22:23
 */
public class Main {

    /**
     * 规则,正则匹配
     */
    private static String  ROLE = "";
    /**
     * 文件后缀
     */
    private static String[] SUFFIX = {"java"};
    /**
     * 路径 C:\Users\DayeShing\code\WorkSpace-2.0\apiface\apiface-core\src\test\java\io\github\dayeshing\apiface\core\model
     */
    private static String PATH = "C:\\Users\\DayeShing\\code\\WorkSpace-2.0\\apiface\\apiface-core\\src\\test\\java";


    /**
     * 扫描路径源码文件
     */
    @Test
    public void scanPath(){
        List<File> files = ScanUtil.scanResources(new File(PATH), SUFFIX, ROLE);
        for (File file : files) {
            System.err.println(file.getAbsolutePath());
        }
    }

    /**
     * 扫描初级文档
     */
    @Test
    public void scanDoc(){
        PATH += "\\io\\github\\dayeshing\\apiface\\core\\model";
        List<File> files = ScanUtil.scanResources(new File(PATH), SUFFIX, ROLE);
        List<ClassMark> marks = new ResolverImpl(new ApiRule(false)).resolveResources(files);
        System.err.println(JSON.toJSONString(marks, SerializerFeature.PrettyFormat));
    }

    /**
     * 生成apiface 格式的接口文档
     */
    @Test
    public void scanApiface1(){
        PATH += "\\io\\github\\dayeshing\\apiface\\core\\model";
        List<File> files = ScanUtil.scanResources(new File(PATH), SUFFIX, ROLE);
        List<ClassMark> marks = new ResolverImpl(new ApiRule(true)).resolveResources(files);
        System.err.println(JSON.toJSONString(marks, SerializerFeature.PrettyFormat));
    }

    /**
     * 生成apiface 格式的接口文档
     */
    @Test
    public void scanApiface(){
        PATH += "\\io\\github\\dayeshing\\apiface\\core\\model";
        List<File> files = ScanUtil.scanResources(new File(PATH), SUFFIX, ROLE);
        List<ClassMark> marks = new ResolverImpl(new ApiRule(false)).resolveResources(files);
        ApiResolver resolver = new ApiResolverImpl(Main.class.getClassLoader());
        resolver.resolver(marks);
        System.err.println(JSON.toJSONString(resolver.getFields(), SerializerFeature.PrettyFormat));
    }

    @Test
    public void scanSwagger(){
        PATH += "\\io\\github\\dayeshing\\apiface\\core\\model";
        List<File> files = ScanUtil.scanResources(new File(PATH), SUFFIX, ROLE);
        List<ClassMark> marks = new ResolverImpl(new ApiRule(false)).resolveResources(files);
        ApiResolver resolver = new ApiResolverImpl(Main.class.getClassLoader());
        resolver.resolver(marks);
        JSONObject transform = new Swagger2((ApiResolverImpl)resolver).transform(resolver.getActions(), resolver.getFields());
        System.err.println(JSON.toJSONString(transform, SerializerFeature.PrettyFormat));

    }
//    public static void main(String[] args) {
////        //"com.daysh.([a-z]+.)+controller.[a-zA-Z]+"
//        List<File> files = ScanUtil.scanResources("C:\\code\\WorkSpace-2.0\\apiface\\apiface-example\\src\\main\\java\\com\\daysh\\apiface", new String[]{"java"}, "");
////        for (File file : files) {
////            System.err.println(file.getAbsolutePath());
////        }
//        List<ClassMark> marks = new ResolverImpl(new ApiRule()).resolveResources(files);
//        ApiResolver resolver = new ApiResolverImpl(Main.class.getClassLoader());
//        resolver.resolver(marks);
//        System.err.println(JSON.toJSONString(new Swagger2().transform(resolver.getActions(),resolver.getFields())));
//////        ;
////        Class<?>[] interfaces = DD.class.getInterfaces();
////        for (Class<?> anInterface : interfaces) {
////            System.err.println(anInterface.getName());
////        }
//    }

    @Before
    public void before(){
        System.err.println("==before =======================");
    }

    @After
    public void after(){
        System.err.println("==after  =======================");
    }
}
