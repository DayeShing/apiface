package com.daysh.apiface.springmvc.controller;

import com.daysh.apiface.controller.GridReturn;
import com.daysh.apiface.model.Hi;
import org.springframework.web.bind.annotation.*;

/**
 * MethodController的接口类描述
 * @action Method
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/17 10:14
 */
@RestController
@RequestMapping(value = "method")
public class MethodController {

    /**
     * 同一个方法的不同请求
     * @param hi |com.daysh.apiface.model.Hi|hi
     * @summary 摘要2
     * @error 错误描述
     * @return com.daysh.apiface.model.Hi|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/17 10:19
     */
    @RequestMapping(value = "hello",method = RequestMethod.POST)
    public Object hello(@RequestBody Hi hi){
        return GridReturn.success(hi);
    }

    /**
     * 同一个方法的不同请求
     * @param id |String|hi
     * @param name |String|非必须|aa|form|name
     * @summary 摘要2
     * @error 错误描述
     * @return com.daysh.apiface.model.Hi|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/17 10:19
     */
    @RequestMapping(value = "hello",method = RequestMethod.GET)
    public Object hello1(Hi hi){
        return GridReturn.success(hi);
    }

    /**
     * 同一个方法的不同请求
     * @param id |String|hi
     * @param name |String|name
     * @summary 摘要2
     * @error 错误描述
     * @return com.daysh.apiface.model.Hi|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/17 10:19
     */
    @RequestMapping(value = "hello",method = RequestMethod.HEAD)
    public Object hello(@RequestHeader(name = "id") String id, @RequestHeader(name = "name") String name){
        return GridReturn.success(new Hi(id,name));
    }
}
