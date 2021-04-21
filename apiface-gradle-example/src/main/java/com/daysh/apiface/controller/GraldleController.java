package com.daysh.apiface.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GraldleController的接口类描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @action graldle
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 */
@RestController
@RequestMapping("first")
public class GraldleController {

    /**
     * 保存活动(从swagger换成了javadoc整个人就像如厕后，身心通泰)(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param activities|String|参数描述
     * @summary 测试
     * @return String|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    @RequestMapping(value = "test")
    public Object test(String activities) {
        return activities;
    }

}
