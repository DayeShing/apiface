package io.github.dayeshing.apiface.core.resolver;

import io.github.dayeshing.apiface.core.comment.Mark;

import java.io.File;
import java.util.List;

/**
 * @Description: <p> 文件解析类接口 </p>
 * @ClassName: FileResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.FileResolver
 * @Author： Daye Shing
 * @Date： 2020-08-14 20:19
 */
public interface FileResolver<T extends Mark> {

    /**
     * resolve
     * @description: <p> 执行解析 </p>
     * @methodName resolve
     * @param files - 要解析的所有java源代码文件的绝对路径
     * @return java.util.List<io.github.dayeshing.apiface.core.comment.Mark> -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 11:20
     * @since 1.0
     */
    List<T> resolve(List<String> files);

    /**
     * resolveResources
     * @description: <p> 执行解析 </p>
     * @methodName resolveResources
     * @param files - 要解析的所有java源代码文件的绝对路径
     * @return java.util.List<io.github.dayeshing.apiface.core.comment.Mark> -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 11:20
     * @since 1.0
     */
    List<T> resolveResources(List<File> files);

    /**
     * resolve
     * @description: <p> 执行解析 </p>
     * @methodName resolve
     * @param file - 要解析的所有java源代码文件的绝对路径
     * @return java.util.List<io.github.dayeshing.apiface.core.comment.Mark> -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 11:20
     * @since 1.0
     */
    List<T> resolve(String file);

    /**
     * resolveResources
     * @description: <p> 执行解析 </p>
     * @methodName resolveResources
     * @param file - 要解析的所有java源代码文件的绝对路径
     * @return java.util.List<io.github.dayeshing.apiface.core.comment.Mark> -
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 11:20
     * @since 1.0
     */
    List<T> resolve(File file);
}
