package io.github.dayeshing.apiface.core.api.meta;

import io.github.dayeshing.apiface.core.bean.Annotation;
import io.github.dayeshing.apiface.core.enums.AnnotationSupportEnum;

import java.util.Map;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Anno
 * @ClassAllName: io.github.dayeshing.apiface.core.api.meta.Anno
 * @Author： Daye Shing
 */
public interface Anno {

    boolean exists(AnnotationSupportEnum ... e);

    Annotation getAnno(AnnotationSupportEnum e);

    void setAnnos(Map<String, Annotation> annos);
}
