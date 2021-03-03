/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.servlet
 * @Author： Daye Shing
 * @CreateTime： 2021-03-02 01:44
 * @Description: <p>  </p>
 */
package com.daysh.apiface.servlet;

import com.daysh.apiface.model.Hi;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HiServlet的描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
 * @action Servlet的实现方式
 * @uri servlet
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 */
@WebServlet(name = "HiServlet",urlPatterns = "/servlet/hi",asyncSupported = true)
public class HiServlet extends HttpServlet {

    /**
     * service(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param request
     * @param response
     * @ignore
     * @return void|描述
     * @author Daye Shing
     * @since 1.0
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        response.getWriter().print(sayHello(new Hi(id, name)));
    }

    /**
     * restlet 的 hello world(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param hi|com.daysh.apiface.model.Hi|必须|hi
     * @summary 获取sayHello
     * @method post,get
     * @uri hi
     * @return java.lang.String|id和名称
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     */
    public String sayHello(Hi hi){
        if(StringUtils.isEmpty(hi.getName())){
            hi.setName("aa");
        }
        return String.format("%s->id=%s&name=%s",getClass().getName(),hi.getId(),hi.getName());
    }
}
