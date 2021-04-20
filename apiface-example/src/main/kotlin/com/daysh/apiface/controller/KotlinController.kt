package com.daysh.apiface.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * KotlinController的接口类描述
 * @action kotlin
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/17 6:56
 */
@RestController
@RequestMapping("kotlin")
class KotlinController {

    /**
     * json 数组
     * @param hots |java.util.List<com.daysh.apiface.model.Hello>|hot
     * @summary 摘要
     * @error 错误描述
     * @return com.daysh.apiface.model.Hello|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/17 6:54
     */
    @GetMapping("hi")
    fun sayHis( hots : String): String {
        return hots;
    }

}