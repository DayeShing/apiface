 /**
 * @BelongsProject： apiface
 * @BelongsPackage： PACKAGE_NAME
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 22:23
 * @Description: <p>  </p>
 */
 package io.github.dayeshing.apiface.core;

import com.alibaba.fastjson.JSON;
import io.github.dayeshing.apiface.core.api.ApiRule;
import io.github.dayeshing.apiface.core.api.resolver.ApiResolver;
import io.github.dayeshing.apiface.core.api.resolver.ApiResolverImpl;
import io.github.dayeshing.apiface.core.comment.impl.ClassMark;
import io.github.dayeshing.apiface.core.resolver.impl.ResolverImpl;
import io.github.dayeshing.apiface.core.swagger.v2.Swagger2;
import io.github.dayeshing.apiface.core.util.ScanUtil;

import java.io.File;
import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: io.github.dayeshing.apiface.core.Main
 * @ClassAllName: PACKAGE_NAME.io.github.dayeshing.apiface.core.Main
 * @Author： Daye Shing
 * @Date： 2020-08-15 22:23
 */
public class Main {

    public static void main(String[] args) {
//        //"com.daysh.([a-z]+.)+controller.[a-zA-Z]+"
        List<File> files = ScanUtil.scanResources("C:\\code\\WorkSpace-2.0\\apiface\\apiface-core\\src\\main\\java\\example", null, "");
//        for (File file : files) {
//            System.err.println(file.getAbsolutePath());
//        }
        List<ClassMark> marks = new ResolverImpl(new ApiRule()).resolveResources(files);
        ApiResolver resolver = new ApiResolverImpl(Main.class.getClassLoader());
        resolver.resolver(marks);
        System.err.println(JSON.toJSONString(new Swagger2().transform(resolver.getActions(),resolver.getFields())));
////        ;
//        Class<?>[] interfaces = DD.class.getInterfaces();
//        for (Class<?> anInterface : interfaces) {
//            System.err.println(anInterface.getName());
//        }
    }
}
