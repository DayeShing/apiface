package io.github.dayeshing.apiface.core.resolver;

import com.alibaba.fastjson.JSON;
import io.github.dayeshing.apiface.core.bean.Annotation;
import io.github.dayeshing.apiface.core.support.spring.RestController;

/**
 * @Description: <p> TODO </p>
 * @ClassName: AbstractAnnotationResolver
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.AbstractAnnotationResolver
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
