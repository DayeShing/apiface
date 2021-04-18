/**
 *  apiface
 *  com.daysh.apiface.restlet
 * @author  Daye Shing
 * @date 2021-03-02 01:17
 *<p>  </p>
 */
package com.daysh.apiface.restlet;

import com.alibaba.fastjson.JSON;
import com.daysh.apiface.model.Hi;
import com.daysh.apiface.util.BodyUtil;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * HelloRestlet的接口类描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @action Restlet的实现方式
 * @uri restlet
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/3/2 14:51
 */
@Component("hiJson")
@Scope("prototype")
public class HiJsonRestlet extends AbstractServerResource {

    /**
     * post请求(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * 通过Representation来获取请求参数
     * @ignore
     * @param entity
     * @return org.restlet.representation.Representation
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     */
    @Post
    public Representation sayHello(Representation entity) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Hi hi = JSON.parseObject(new String(BodyUtil.readAsBytes(request)), Hi.class);
        String requestURI = request.getRequestURI();
        return new StringRepresentation(JSON.toJSONString(sayHiJson(hi,requestURI)));
    }

    /**
     * 方法描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param hi |com.daysh.apiface.model.Hi|必须||body|hi
     * @param path |String|必须||path|参数描述
     * @summary 获取sayHi
     * @uri json/{path}
     * @method post
     * @return com.daysh.apiface.model.Hi|描述
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     * @date 2021/3/2 16:36
     */
    public Hi sayHiJson(Hi hi, String path){
        hi.setName(hi.getName() + path);
        return hi;
    }

}
