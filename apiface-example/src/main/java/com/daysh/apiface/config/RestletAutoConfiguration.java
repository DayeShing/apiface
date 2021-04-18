/**
 *  apiface
 *  com.daysh.apiface.config
 * @author  Daye Shing
 * @date 2021-03-02 00:59
 *<p>  </p>
 */
package com.daysh.apiface.config;

import org.restlet.ext.spring.RestletFrameworkServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * 整合Restlet到Spring
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/3/2 14:48
 */
@Configuration
@ImportResource(RestletAutoConfiguration.CONF)
public class RestletAutoConfiguration {

    public static final String CONF = "classpath*:spring/restlet-servlet.xml";

    /**
     * 注册servlet
     * @return org.springframework.boot.web.servlet.ServletRegistrationBean|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/3/2 14:49
     */
    @Bean
    public ServletRegistrationBean getRestletServletBean() {
        RestletFrameworkServlet restletFrameworkServlet = new RestletFrameworkServlet();
        restletFrameworkServlet.setContextConfigLocation(RestletAutoConfiguration.CONF);
        ServletRegistrationBean bean = new ServletRegistrationBean(restletFrameworkServlet);
        bean.addUrlMappings("/restlet/*");
        bean.setName("restlet");
        bean.setAsyncSupported(true);
        bean.setOrder(1);
        return bean;
    }

}
