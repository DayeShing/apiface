/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.support.spring.resolver
 * @Author： Daye Shing
 * @CreateTime： 2021-02-28 14:45
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.support.spring.resolver;

import com.daysh.apiface.core.resolver.AbstractAnnotationResolver;
import com.daysh.apiface.core.support.spring.BaseElement;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestControllerResolver
 * @ClassAllName: com.daysh.apiface.core.support.spring.resolver.RestControllerResolver
 * @Author： Daye Shing
 */
public class BaseElementResolver extends AbstractAnnotationResolver<BaseElement> {

    @Override
    protected Class<BaseElement> getType() {
        return BaseElement.class;
    }

}
