package io.github.dayeshing.apiface.core.support.spring;

import io.github.dayeshing.apiface.core.bean.Annotation;

/**
 * @Description: <p> TODO </p>
 * @ClassName: BaseElement
 * @ClassAllName: io.github.dayeshing.apiface.core.support.spring.BaseElement
 * @Author： Daye Shing
 */
public class BaseElement implements Annotation {

    String DEFAULT_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

    private String value;

    private String name;

    private boolean required;

    private String defaultValue;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        if(DEFAULT_NONE.equals(defaultValue)){
            return "";
        }
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
