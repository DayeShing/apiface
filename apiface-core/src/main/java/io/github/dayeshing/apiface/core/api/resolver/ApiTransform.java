package io.github.dayeshing.apiface.core.api.resolver;

import io.github.dayeshing.apiface.core.api.meta.ActionGroup;
import io.github.dayeshing.apiface.core.api.meta.FieldGroup;

import java.util.Map;

/**
 * @Description: <p> 接口转换文档接口 </p>
 * @ClassName: ApiTransform
 * @ClassAllName: io.github.dayeshing.apiface.core.api.resolver.ApiTransform
 * @Author： Daye Shing
 */
public interface ApiTransform {

    Object transform(Map<String, ActionGroup> actions,Map<String, FieldGroup> fields);
}
