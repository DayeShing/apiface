/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.servlet
 * @Author： Daye Shing
 * @CreateTime： 2021-03-02 01:44
 * @Description: <p>  </p>
 */
package com.daysh.apiface.servlet;

import com.alibaba.fastjson.JSON;
import com.daysh.apiface.model.Hi;
import com.daysh.apiface.util.BodyUtil;

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
@WebServlet(name = "HiJsonServlet",urlPatterns = "/servlet/json/*",asyncSupported = true)
public class HiJsonServlet extends HttpServlet {

    /**
     * service
     * @param request
     * @param response
     * @ignore
     * @return void|描述
     * @author Daye Shing
     * @since 1.0
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Hi hi = JSON.parseObject(new String(BodyUtil.readAsBytes(request)), Hi.class);
        String requestURI = request.getRequestURI();
        response.getWriter().print(JSON.toJSONString(sayHiJson(hi,requestURI)));
    }

    /**
     * 方法描述(我们这样做不是因为我们想改变你，而是我们可以这么做)
     * @param hi |com.daysh.apiface.model.Hi|必须||body|hi
     * @param path |String|必须||path|参数描述
     * @summary 获取sayHello
     * @uri json/{path}
     * @method post
     * @return com.daysh.apiface.model.Hi|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/3/2 16:36
     */
    public Hi sayHiJson(Hi hi, String path){
        hi.setName(hi.getName() + path);
        return hi;
    }
}
