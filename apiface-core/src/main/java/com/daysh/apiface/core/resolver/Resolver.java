package com.daysh.apiface.core.resolver;

import com.daysh.apiface.core.comment.Mark;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;

import java.util.List;

/**
 * @Description: <p> 节点转换 </p>
 * @ClassName: NodeResolver
 * @ClassAllName: com.daysh.apiface.core.resolver.NodeResolver
 * @Author： Daye Shing
 * @Date： 2020-08-14 13:17
 */
public interface Resolver<T extends Mark> {

    /**
     * resolver
     * @description: <p> 转换类 </p>
     * @methodName resolver
     * @param unit -
     * @return java.util.List<T> - 结果类
     * @throws
     * @author Daye Shing
     * @date 2020/8/14 20:12
     * @since 1.0
     */
    List<T> resolver(CompilationUnit unit);

}
