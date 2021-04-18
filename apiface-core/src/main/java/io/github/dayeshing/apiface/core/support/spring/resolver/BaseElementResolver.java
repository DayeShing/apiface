package io.github.dayeshing.apiface.core.support.spring.resolver;

import io.github.dayeshing.apiface.core.resolver.AbstractAnnotationResolver;
import io.github.dayeshing.apiface.core.support.spring.BaseElement;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestControllerResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.support.spring.resolver.RestControllerResolver
 * @Author： Daye Shing
 */
public class BaseElementResolver extends AbstractAnnotationResolver<BaseElement> {

    @Override
    protected Class<BaseElement> getType() {
        return BaseElement.class;
    }

}
