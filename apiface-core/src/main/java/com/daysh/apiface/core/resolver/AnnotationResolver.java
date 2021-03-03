package com.daysh.apiface.core.resolver;

import com.daysh.apiface.core.bean.Annotation;
import com.github.javaparser.ast.CompilationUnit;


/**
 * @Description: <p> 注解支持接口 </p>
 * @ClassName: AnnotationSupportResolver
 * @ClassAllName: com.daysh.apiface.core.resolver.AnnotationSupportResolver
 * @Author： Daye Shing
 */
public interface AnnotationResolver<T extends Annotation> {

    T resolver(java.lang.annotation.Annotation anno);
}
