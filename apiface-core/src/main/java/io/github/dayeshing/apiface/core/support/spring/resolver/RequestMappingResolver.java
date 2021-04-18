package io.github.dayeshing.apiface.core.support.spring.resolver;

import io.github.dayeshing.apiface.core.enums.AnnotationSupportEnum;
import io.github.dayeshing.apiface.core.resolver.AbstractAnnotationResolver;
import io.github.dayeshing.apiface.core.support.spring.RequestMapping;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestControllerResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.support.spring.resolver.RestControllerResolver
 * @Author： Daye Shing
 */
public class RequestMappingResolver extends AbstractAnnotationResolver<RequestMapping> {

    @Override
    protected Class<RequestMapping> getType() {
        return RequestMapping.class;
    }

    @Override
    protected RequestMapping after(RequestMapping requestMapping, Annotation anno) {
        List<String> method = requestMapping.getMethod();
        if (ObjectUtil.isEmpty(method)) {
            method = new ArrayList<>();
            switch (AnnotationSupportEnum.valueOf(anno.annotationType().getSimpleName())) {
                case DeleteMapping:
                    method.add("DELETE");
                    break;
                case PatchMapping:
                    method.add("PATCH");
                    break;
                case PostMapping:
                    method.add("POST");
                    break;
                case PutMapping:
                    method.add("PUT");
                    break;
                default:
                    method.add("GET");
            }
            requestMapping.setMethod(method);
        }
        return requestMapping;
    }
}
