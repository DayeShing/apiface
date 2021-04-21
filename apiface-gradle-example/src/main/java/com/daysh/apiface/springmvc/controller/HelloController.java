/**
 *  apiface
 *  com.daysh.apiface.springmvc.controller
 * @author  Daye Shing
 * @date 2021-02-28 13:01
 *<p>  </p>
 */
package com.daysh.apiface.springmvc.controller;

import com.daysh.apiface.model.Hi;
import org.springframework.web.bind.annotation.*;

/**
 * HelloController的接口类描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @action springmvc的实现方式
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/3/2 15:16
 */
@RestController
@RequestMapping(value = "springmvc",method = RequestMethod.POST)
public class HelloController extends BaseController {

    /**
     * 方法描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param id |string|必须|id参数描述
     * @param name |string|非必须|aa|name参数描述
     * @summary 获取sayHello
     * @return java.lang.String|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     * @date 2021/3/2 15:19
     */
    @Deprecated
    @RequestMapping(value = "hello",method = {RequestMethod.POST,RequestMethod.GET})
    public String sayHello(@RequestParam String id,@RequestParam(defaultValue = "aa",name = "name")String name){
        return String.format("%s->id=%s&name=%s",getClass().getName(),id,name);
    }

    /**
     * 方法描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param hi |com.daysh.apiface.model.Hi|必须|hi
     * @summary 获取sayHello
     * @return java.lang.String|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     * @date 2021/3/2 15:18
     */
    @RequestMapping(value = "hi",method = {RequestMethod.POST,RequestMethod.GET})
    public String sayHi(Hi hi){
        return String.format("%s->id=%s&name=%s",getClass().getName(),hi.getId(),hi.getName());
    }

    /**
     * 返回对象和表示有一个匹配路径(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param hi |com.daysh.apiface.model.Hi|必须|hi
     * @param path |string|path
     * @summary 获取sayHello
     * @return com.daysh.apiface.model.Hi|sayHi
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     * @date 2021/3/2 16:21
     */
    @Deprecated
    @RequestMapping(value = "json/{path}",method = {RequestMethod.POST})
    public Object sayHiJson(@RequestBody Hi hi,@PathVariable String path){
        hi.setName(hi.getName() + path);
        return hi;
    }

    /**
     * 返回对象和表示有一个匹配路径(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param hi |com.daysh.apiface.model.Hi|必须|hi
     * @param path |string|路径
     * @param userId |string|用户id
     * @summary 获取sayHello
     * @return com.daysh.apiface.model.Hi|sayHi
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     * @date 2021/3/2 16:21
     */
    @RequestMapping(value = "json/{path}/{userId}",method = {RequestMethod.POST,RequestMethod.GET})
    public Object sayHiPath(Hi hi,@PathVariable String path,@PathVariable String userId){
        hi.setName(hi.getName() + path + userId);
        return hi;
    }

    /**
     * 详细描述
     * @param hi |com.daysh.apiface.model.Hi|必须|hi
     * @param path |string|参数描述
     * @param userId |string|参数描述
     * @param aa |string|参数描述
     * @param json |string|参数描述
     * @param cookie |string|参数描述
     * @summary sayHiPath
     * @error 错误描述
     * @return com.daysh.apiface.model.Hi|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/3/7 6:05
     */
    @PostMapping(value = "json/{path}/{userId}/{aa}")
    public Object sayHiPath(@RequestBody Hi hi,
                            @PathVariable String path,
                            @PathVariable String userId,
                            @PathVariable String aa,
                            @RequestHeader String json,
                            @CookieValue String cookie
    ){
        hi.setName(hi.getName() + path + userId);
        return hi;
    }
}
