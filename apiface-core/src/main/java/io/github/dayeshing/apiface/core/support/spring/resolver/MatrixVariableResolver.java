package io.github.dayeshing.apiface.core.support.spring.resolver;

import io.github.dayeshing.apiface.core.resolver.AbstractAnnotationResolver;
import io.github.dayeshing.apiface.core.support.spring.MatrixVariable;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestControllerResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.support.spring.resolver.RestControllerResolver
 * @Author： Daye Shing
 */
public class MatrixVariableResolver extends AbstractAnnotationResolver<MatrixVariable> {

    @Override
    protected Class<MatrixVariable> getType() {
        return MatrixVariable.class;
    }

}
