package io.github.dayeshing.apiface.core.support.spring;

import io.github.dayeshing.apiface.core.bean.Annotation;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestController
 * @ClassAllName: io.github.dayeshing.apiface.core.support.spring.RestController
 * @Author： Daye Shing
 */
public class RestController implements Annotation {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
