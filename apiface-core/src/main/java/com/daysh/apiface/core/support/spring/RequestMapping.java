/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.support.spring
 * @Author： Daye Shing
 * @CreateTime： 2021-02-28 15:10
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.support.spring;

import com.daysh.apiface.core.bean.Annotation;

import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RequestMapping
 * @ClassAllName: com.daysh.apiface.core.support.spring.RequestMapping
 * @Author： Daye Shing
 */
public class RequestMapping implements Annotation {

    private String name;

    private List<String> value;

    private List<String> path;

    private List<String> method;

    private List<String> params;

    private List<String> headers;

    private List<String> consumes;

    private List<String> produces;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public List<String> getMethod() {
        return method;
    }

    public void setMethod(List<String> method) {
        this.method = method;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }
}
