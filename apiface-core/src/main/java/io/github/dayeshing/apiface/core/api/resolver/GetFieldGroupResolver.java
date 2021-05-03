package io.github.dayeshing.apiface.core.api.resolver;

import io.github.dayeshing.apiface.core.api.meta.FieldGroup;

/**
 * GetFieldGroupResolver的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @date 2021-04-26 12:03
 * @since 1.0
 */
public interface GetFieldGroupResolver {

    /**
     * 通过ref获取一个不存在的对象
     * @param ref |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @return io.github.dayeshing.apiface.core.api.meta.FieldGroup|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/26 12:04
     */
    FieldGroup getField(String ref);

    /**
     * 排除规则
     * @param ref |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @param clazz |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @ignore
     * @return boolean|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/26 12:20
     */
    boolean exclude(String ref,Class clazz);

}
