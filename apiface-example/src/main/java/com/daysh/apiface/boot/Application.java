/**
 *  apiface
 *  com.daysh.apiface.boot
 * @author  Daye Shing
 * @date 2021-02-28 05:52
 *<p>  </p>
 */
package com.daysh.apiface.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application的描述
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/3/2 14:47
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.daysh")
@ServletComponentScan(basePackages = "com.daysh")
public class Application extends SpringBootServletInitializer {

    /**
     * 方法描述
     * @param args |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @return void|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/3/2 14:47
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    /**
     * 方法描述
     * @param application |参数类型|必须|默认值|参数类型 body/form/path|参数描述
     * @return org.springframework.boot.builder.SpringApplicationBuilder|描述
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     * @date 2021/3/2 14:48
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
