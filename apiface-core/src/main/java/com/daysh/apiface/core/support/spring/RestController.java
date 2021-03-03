/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.support.spring
 * @Author： Daye Shing
 * @CreateTime： 2021-02-28 14:43
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.support.spring;

import com.daysh.apiface.core.bean.Annotation;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RestController
 * @ClassAllName: com.daysh.apiface.core.support.spring.RestController
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
