package io.github.dayeshing.apiface.core.support.spring.resolver;

import io.github.dayeshing.apiface.core.resolver.AbstractAnnotationResolver;
import io.github.dayeshing.apiface.core.support.spring.RestController;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestControllerResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.support.spring.resolver.RestControllerResolver
 * @Author： Daye Shing
 */
public class ControllerResolver extends AbstractAnnotationResolver<RestController> {

    @Override
    protected Class<RestController> getType() {
        return RestController.class;
    }

}
