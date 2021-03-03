package com.daysh.apiface.core.api.meta;

import com.daysh.apiface.core.bean.Annotation;
import com.daysh.apiface.core.enums.AnnotationSupportEnum;

import java.util.Map;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Anno
 * @ClassAllName: com.daysh.apiface.core.api.meta.Anno
 * @Author： Daye Shing
 */
public interface Anno {

    boolean exists(AnnotationSupportEnum ... e);

    Annotation getAnno(AnnotationSupportEnum e);

    void setAnnos(Map<String, Annotation> annos);
}
