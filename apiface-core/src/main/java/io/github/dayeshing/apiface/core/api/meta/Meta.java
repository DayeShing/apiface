package io.github.dayeshing.apiface.core.api.meta;

/**
 * @Description: <p> 接口 </p>
 * @ClassName: Api
 * @ClassAllName: io.github.dayeshing.apiface.core.meta.Api
 * @Author： Daye Shing
 * @Date： 2020-08-15 16:42
 */
public interface Meta {

    String getName();

    String getDesc();

    boolean isDeprecated();

    boolean isHidden();

    /**
     * 存在注释文档
     */
    boolean isExists();
}
