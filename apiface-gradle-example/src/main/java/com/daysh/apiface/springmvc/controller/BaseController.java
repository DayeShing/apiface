/**
 *  apiface
 *  com.daysh.apiface.springmvc.controller
 * @author  Daye Shing
 * @date 2021-02-28 12:49
 *<p>  </p>
 */
package com.daysh.apiface.springmvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 一个基本接口类(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @action base
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/3/2 15:26
 * @hidden
 */
public class BaseController {


    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    /**
     * index(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @return void|token字符串(返回值描述)
     * @author Daye Shing
     * @uri index
     * @since 1.0
     */
    @RequestMapping
    public ModelAndView index() {
        String ctxPath = request.getContextPath();
        String uri = request.getRequestURI();
        int tok = uri.indexOf(";");
        int end = uri.length();
        if (tok >= 0) {
            end = tok;
        }
        uri = uri.substring(ctxPath.length(), end);
        if (!uri.endsWith("/")) {
            uri += "/";
        }
        uri += "index";
        return new ModelAndView(uri);
    }

    /**
     * 获取token,但是接口api忽略这条记录
     * @ignore
     * @return java.lang.String|token字符串(返回值描述)
     * @author Daye Shing
     * @since 1.0
     */
    public String token(){
        return "token";
    }

}
