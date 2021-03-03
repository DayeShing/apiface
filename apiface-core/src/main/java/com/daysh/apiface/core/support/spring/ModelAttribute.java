/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.support.spring
 * @Author： Daye Shing
 * @CreateTime： 2021-02-28 16:06
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.support.spring;

/**
 * @Description: <p> TODO </p>
 * @ClassName: ModelAttribute
 * @ClassAllName: com.daysh.apiface.core.support.spring.ModelAttribute
 * @Author： Daye Shing
 */
public class ModelAttribute extends BaseElement {
    private boolean binding;

    public boolean isBinding() {
        return binding;
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }
}
