package com.daysh.apiface.springmvc.controller;

import com.daysh.apiface.model.Hello;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ArrayController的接口类描述
 * @action array
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/4/17 6:56
 */
@RestController
@RequestMapping(value = "array",method = RequestMethod.POST)
public class ArrayController {

    /**
     * json 数组
     * @param hot |java.util.List<com.daysh.apiface.model.Hello>|hot
     * @summary 摘要
     * @error 错误描述
     * @return com.daysh.apiface.model.Hello|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/17 6:54
     */
    @RequestMapping(value = "hi",method = {RequestMethod.POST})
    public Object sayHi(@RequestBody List<Hello> hot){
        return hot;
    }

    /**
     * json 数组2
     * @param hot |java.util.List<com.daysh.apiface.model.Hello>|hot
     * @summary 摘要2
     * @error 错误描述
     * @return com.daysh.apiface.model.Hello[]|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/17 6:54
     */
    @RequestMapping(value = "hr",method = {RequestMethod.POST})
    public Object sayHr(@RequestBody List<Hello> hot){
        return hot;
    }

    /**
     * json 数组23
     * @param hot |String[]|hot
     * @summary 摘要23
     * @error 错误描述
     * @return string[]|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/4/17 6:54
     */
    @RequestMapping(value = "ha",method = {RequestMethod.POST})
    public Object sayHa(@RequestBody List<String> hot){
        return hot;
    }

}
