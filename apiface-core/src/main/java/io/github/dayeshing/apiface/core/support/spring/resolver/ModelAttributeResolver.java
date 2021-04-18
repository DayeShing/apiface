package io.github.dayeshing.apiface.core.support.spring.resolver;

import io.github.dayeshing.apiface.core.resolver.AbstractAnnotationResolver;
import io.github.dayeshing.apiface.core.support.spring.ModelAttribute;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestControllerResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.support.spring.resolver.RestControllerResolver
 * @Author： Daye Shing
 */
public class ModelAttributeResolver extends AbstractAnnotationResolver<ModelAttribute> {

    @Override
    protected Class<ModelAttribute> getType() {
        return ModelAttribute.class;
    }

}
