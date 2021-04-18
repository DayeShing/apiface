/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.util
 * @Author： Daye Shing
 * @CreateTime： 2020-08-14 08:53
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        List<File> ret = new ArrayList<>(50);
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

}
