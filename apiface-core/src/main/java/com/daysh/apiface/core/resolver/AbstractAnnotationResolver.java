/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.resolver
 * @Author： Daye Shing
 * @CreateTime： 2021-02-28 14:47
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.resolver;

import com.alibaba.fastjson.JSON;
import com.daysh.apiface.core.bean.Annotation;
import com.daysh.apiface.core.support.spring.RestController;

/**
 * @Description: <p> TODO </p>
 * @ClassName: AbstractAnnotationResolver
 * @ClassAllName: com.daysh.apiface.core.resolver.AbstractAnnotationResolver
 * @Author： Daye Shing
 */
public abstract class AbstractAnnotationResolver<T extends Annotation> implements AnnotationResolver<T>{

    @Override
    public T resolver(java.lang.annotation.Annotation anno) {
        return after(JSON.parseObject(JSON.toJSONString(anno), getType()),anno);
    }

    protected T after(T t , java.lang.annotation.Annotation anno){
        return t;
    }

    protected abstract Class<T> getType();
}
