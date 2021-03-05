package com.daysh.apiface.core.support.spring.resolver;

import com.daysh.apiface.core.resolver.AbstractAnnotationResolver;
import com.daysh.apiface.core.support.spring.MatrixVariable;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestControllerResolver
 * @ClassAllName: com.daysh.apiface.core.support.spring.resolver.RestControllerResolver
 * @Author： Daye Shing
 */
public class MatrixVariableResolver extends AbstractAnnotationResolver<MatrixVariable> {

    @Override
    protected Class<MatrixVariable> getType() {
        return MatrixVariable.class;
    }

}
