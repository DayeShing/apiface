/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.util
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 08:53
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * @Description: <p> 扫描类 </p>
 * @ClassName: ScanUtil
 * @ClassAllName: io.github.dayeshing.apiface.core.util.ScanUtil
 * @Author： Daye Shing
 * @Date： 2020-08-14 08:53
 */
public class ScanUtil {

    /**
     * scanResources
     *
     * @param path    - 源码包位置
     * @param suffixs - 文件匹配
     * @param rule    - 包匹配规则
     * @return java.util.List<java.io.File> -
     * @throws
     * @description: <p> 获取源文件 </p>
     * @methodName scanResources
     * @author Daye Shing
     * @date 2020/8/14 9:35
     * @since 1.0
     */
    public static List<File> scanResources(String path, String[] suffixs, String rule) {
        return scanResources(new File(path), suffixs, rule);
    }

    /**
     * scanResources
     *
     * @param path    - 源码包位置
     * @param suffixs - 文件匹配
     * @param rule    - 包匹配规则
     * @return java.util.List<java.io.File> -
     * @throws
     * @description: <p> 获取源文件 </p>
     * @methodName scanResources
     * @author Daye Shing
     * @date 2020/8/14 9:35
     * @since 1.0
     */
    public static List<File> scanResources(File path, String[] suffixs, String rule) {
        if (path.exists() && path.isDirectory()) {
            return scanFiles(path, suffixs, rule, path.getAbsolutePath());
        }
        return null;
    }

    /**
     * scanFiles
     *
     * @param file    - 源码位置
     * @param suffixs - 文件匹配
     * @param rule    - 包匹配规则
     * @param top     - 源码包位置
     * @return java.util.List<java.io.File> -
     * @throws
     * @description: <p> 获取源文件 </p>
     * @methodName scanFiles
     * @author Daye Shing
     * @date 2020/8/14 9:35
     * @since 1.0
     */
    public static List<File> scanFiles(File file, String[] suffixs, String rule, String top) {
        List<File> ret = new ArrayList<File>(50);
        File[] fs = file.listFiles();
        for (File f : fs) {
            // 校验是否合法-
            if (f.isDirectory()) {
                ret.addAll(scanFiles(f, suffixs, rule, top));
            } else {
                if (rule(f, rule, top)) {
                    if (suffixs == null || suffixs.length == 0) {
                        ret.add(f);
                        continue;
                    }
                    for (String suffix : suffixs) {
                        if (f.getName().endsWith(suffix)) {
                            ret.add(f);
                            break;
                        }
                    }
                }
            }
        }
        return ret;
    }

    /**
     * scanFiles
     *
     * @param file - 源码位置
     * @param rule - 包匹配规则
     * @param top  - 源码包位置
     * @return java.util.List<java.io.File> -
     * @throws
     * @description: <p> 校验源文件是否是所需的文件 </p>
     * @methodName scanFiles
     * @author Daye Shing
     * @date 2020/8/14 9:35
     * @since 1.0
     */
    public static boolean rule(File file, String rule, String top) {
        String dot = ".";
        String resource = file.getAbsolutePath().replace(top, "");
        int i = resource.indexOf(dot);
        if (i > -1) {
            resource = resource.substring(0, i);
        }
        resource = resource.replace(File.separator, dot);
        if (resource.startsWith(dot)) {
            resource = resource.substring(1);
        }
        if (ObjectUtil.isEmpty(rule)) {
            return true;
        }
        return Pattern.matches(rule, resource);
    }

    public static List<Class<?>> getClasses(String packageName){
        //第一个class类的集合
        List<Class<?>> classes = new ArrayList<Class<?>>();
        //是否循环迭代
        boolean recursive = true;
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代下去
            while (dirs.hasMoreElements()){
                //获取下一个元素
                URL url = dirs.nextElement();
                //得到协议的名称
                String protocol = url.getProtocol();
                //如果是以文件的形式保存在服务器上
                if ("jar".equals(protocol)){
                    //如果是jar包文件
                    //定义一个JarFile
                    JarFile jar;
                    try {
                        //获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        //从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        //同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            //如果是以/开头的
                            if (name.charAt(0) == '/') {
                                //获取后面的字符串
                                name = name.substring(1);
                            }
                            //如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                //如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    //获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                //如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive){
                                    //如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        //去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            //添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

}
