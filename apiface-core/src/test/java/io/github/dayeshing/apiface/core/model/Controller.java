package io.github.dayeshing.apiface.core.model;

import java.util.Map;

/**
 * Controller的接口类描述
 * @action 分组名称
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/26 10:08
 */
public class Controller {

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
     * 详细描述
     * @param gg |io.github.dayeshing.apiface.core.model.Hello|必须|0|body|参数描述
     * @uri urid
     * @summary 摘要
     * @method post
     * @exclude 排除参数1,排除参数2
     * @error 错误描述
     * @return io.github.dayeshing.apiface.core.model.GridResult<io.github.dayeshing.apiface.core.model.Hello>|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/26 10:08
     */
    public GridResult<Hello> gg2(Hello gg){
        return null;
    }

    /**
     * 详细描述
     * @param dsdsd |io.github.dayeshing.apiface.core.model.Hello|必须|0|body|参数描述
     * @uri urid
     * @summary 摘要
     * @method post
     * @exclude 排除参数1,排除参数2
     * @error 错误描述
     * @return io.github.dayeshing.apiface.core.model.GridResult<io.github.dayeshing.apiface.core.model.Hello>|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/26 10:08
     */
    public GridResult<Hello> gg2f(Map<Hi,Hello> dsdsd){
        return null;
    }

    /**
     * 详细描述
     * @param dsdsd |io.github.dayeshing.apiface.core.model.Hello|必须|0|body|参数描述
     * @uri urid
     * @summary 摘要
     * @method post
     * @exclude 排除参数1,排除参数2
     * @error 错误描述
     * @return io.github.dayeshing.apiface.core.model.GridResult<io.github.dayeshing.apiface.core.model.Hello>|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/26 10:08
     */
    public GridResult<Hello> gg2ff(Map<Hi, java.lang.Boolean> dsdsd){
        return null;
    }
}
