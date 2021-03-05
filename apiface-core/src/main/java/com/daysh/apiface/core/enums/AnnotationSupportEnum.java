package com.daysh.apiface.core.enums;

import com.daysh.apiface.core.bean.Annotation;
import com.daysh.apiface.core.resolver.AnnotationResolver;
import com.daysh.apiface.core.support.spring.resolver.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: <p> 注释支持 </p>
 * @ClassName: AnnotationSupportEnum
 * @ClassAllName: com.daysh.apiface.core.enums.AnnotationSupportEnum
 * @Author： Daye Shing
 */
public enum AnnotationSupportEnum implements AnnotationResolver<Annotation> {

    // class
    Controller(new ControllerResolver()),
    RestController(Controller),

    //method
    CrossOrigin(null),
    ExceptionHandler(null),//Class<? extends Throwable>[] value() default {};
    InitBinder(null),//String[] value() default {};
    ModelAttribute(new ModelAttributeResolver()),
    ResponseStatus(null),

    ResponseBody(null),

    // param
    RequestMapping(new RequestMappingResolver()),
    DeleteMapping(RequestMapping),
    GetMapping(RequestMapping),
    PatchMapping(RequestMapping),
    PostMapping(RequestMapping),
    PutMapping(RequestMapping),

    PathVariable(new BaseElementResolver()),
    RequestBody(PathVariable.resolver),
    RequestParam(PathVariable.resolver),
    CookieValue(PathVariable.resolver),
    MatrixVariable(new MatrixVariableResolver()),
    RequestAttribute(PathVariable.resolver),
    RequestHeader(PathVariable.resolver),
    RequestPart(PathVariable.resolver),
    ;

    private AnnotationResolver resolver;
    private AnnotationSupportEnum associate;

    AnnotationSupportEnum(AnnotationResolver resolver) {
        this.resolver = resolver;
    }

    AnnotationSupportEnum(AnnotationSupportEnum associate) {
        this.associate = associate;
    }

    @Override
    public Annotation resolver(java.lang.annotation.Annotation anno) {
        if(associate != null){
            return associate.resolver(anno);
        }
        if(resolver != null){
            return resolver.resolver(anno);
        }
        return null;
    }

    public static Map<String,Annotation> resolver(java.lang.annotation.Annotation ... annos) {
        Map<String,Annotation> map = new HashMap<>();
        if(annos != null){
            for (java.lang.annotation.Annotation anno : annos) {
                for (AnnotationSupportEnum value : values()) {
                    String name = value.name();
                    if(anno.annotationType().getName().endsWith(name)){
                        if(value.associate != null){
                            name = value.associate.name();
                        }
                        map.put(name, value.resolver(anno));
                    }
                }
            }
        }
        return map;
    }
}
