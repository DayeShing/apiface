/**
 *  apiface
 *  com.daysh.apiface.servlet
 * @author  Daye Shing
 * @date 2021-03-02 01:44
 *<p>  </p>
 */
package com.daysh.apiface.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HelloServlet的描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @action Servlet的实现方式
 * @uri servlet
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/3/2 14:53
 */
@Deprecated
@WebServlet(name = "HelloServlet",urlPatterns = "/servlet/hello",asyncSupported = true)
public class HelloServlet extends HttpServlet {

    /**
     * 方法描述
     * @param request xf
     * @param response xf
     * @ignore
     * @return void|描述
     * @author Daye Shing
     * @since 1.0
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        response.getWriter().print(sayHello(id, name));
    }

    /**
     * restlet 的 hello world(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param id|string|必须|id
     * @param name|string|必须|名称
     * @summary 获取sayHello
     * @method post,get
     * @uri hello
     * @return java.lang.String|id和名称
     * @author Daye Shing | 896379914@qq.com
     * @error 错误
     * @since 1.0
     */
    public String sayHello(String id,String name){
        return String.format("%s->id=%s&name=%s",getClass().getName(),id,name);
    }
}
