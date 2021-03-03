/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.restlet
 * @Author： Daye Shing
 * @CreateTime： 2021-03-02 01:17
 * @Description: <p>  </p>
 */
package com.daysh.apiface.restlet;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
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
@Component("hello")
@Scope("prototype")
@Slf4j
@Deprecated
public class HelloRestlet extends ServerResource {

    /**
     * get请求(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * 通过HttpServletRequest获取请求参数
     * @ignore
     * @return org.restlet.representation.Representation
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     */
    @Get("xml|json")
    public Representation sayHello() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        log.debug("post");
        return new StringRepresentation(sayHello(id,name));
    }

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
        Form form = new Form(entity);
        String id = form.getValues("id");
        String name = form.getFirstValue("name");
        log.debug("post");
        return new StringRepresentation(sayHello(id,name));
    }

    /**
     * restlet 的 hello world(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param id|string|必须|id
     * @param name|string|非必须|aa|名称
     * @summary 获取sayHello
     * @method post,get
     * @uri hello
     * @return java.lang.String|id和名称
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     */
    public String sayHello(String id,String name){
        if(StringUtils.isEmpty(name)){
            name = "aa";
        }
        return String.format("%s->id=%s&name=%s",getClass().getName(),id,name);
    }
}
