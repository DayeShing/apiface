package io.github.dayeshing.apiface.core.model;

/**
 * Action的接口类描述
 * @action 分组名称s
 * @uri uri
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/26 10:25
 */
public class Action<T> {
    /**
     * 详细描述
     * @param gg |String|必须|0|form|参数描述
     * @uri uri
     * @summary 摘要
     * @method post,get
     * @exclude 排除参数1,排除参数2
     * @error 错误描述
     * @return io.github.dayeshing.apiface.core.model.Hello|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/26 10:08
     */
    public Hello gg(String gg){
        return null;
    }

    /**
     * 不存在的
     * @param gg |io.github.dayeshing.apiface.core.model.Girl|必须|0|form|参数描述
     * @uri urih
     * @summary 不存在的
     * @method post,get
     * @exclude 排除参数1,排除参数2
     * @error 错误描述
     * @return io.github.dayeshing.apiface.core.model.Girl|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/26 10:08
     */
    public Girl ggh(Girl gg){
        return null;
    }
}
