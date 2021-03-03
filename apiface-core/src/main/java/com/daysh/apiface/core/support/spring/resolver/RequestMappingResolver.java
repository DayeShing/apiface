/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.support.spring.resolver
 * @Author： Daye Shing
 * @CreateTime： 2021-02-28 14:45
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.support.spring.resolver;

import com.daysh.apiface.core.enums.AnnotationSupportEnum;
import com.daysh.apiface.core.resolver.AbstractAnnotationResolver;
import com.daysh.apiface.core.support.spring.RequestMapping;
import com.daysh.apiface.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestControllerResolver
 * @ClassAllName: com.daysh.apiface.core.support.spring.resolver.RestControllerResolver
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
