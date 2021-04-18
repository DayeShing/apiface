package io.github.dayeshing.apiface.core.resolver;

import io.github.dayeshing.apiface.core.bean.Annotation;
import com.github.javaparser.ast.CompilationUnit;


/**
 * @Description: <p> 注解支持接口 </p>
 * @ClassName: AnnotationSupportResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.AnnotationSupportResolver
 * @Author： Daye Shing
 */
public interface AnnotationResolver<T extends Annotation> {

    T resolver(java.lang.annotation.Annotation anno);
}
