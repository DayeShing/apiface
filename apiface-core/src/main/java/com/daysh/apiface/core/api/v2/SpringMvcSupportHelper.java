/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.api.v2
 * @Author： Daye Shing
 * @CreateTime： 2021-02-28 17:01
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.api.v2;

import com.alibaba.fastjson.JSON;
import com.daysh.apiface.core.api.meta.*;
import com.daysh.apiface.core.bean.Annotation;
import com.daysh.apiface.core.comment.impl.ClassMark;
import com.daysh.apiface.core.comment.impl.MethodMark;
import com.daysh.apiface.core.comment.impl.ParamMark;
import com.daysh.apiface.core.enums.ParamEnum;
import com.daysh.apiface.core.support.spring.BaseElement;
import com.daysh.apiface.core.util.ObjectUtil;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.daysh.apiface.core.enums.AnnotationSupportEnum.*;

/**
 * @Description: <p> TODO </p>
 * @ClassName: SpringMvcSupportHelper
 * @ClassAllName: com.daysh.apiface.core.api.v2.SpringMvcSupportHelper
 * @Author： Daye Shing
 */
public class SpringMvcSupportHelper {

    public static void loadAnnotation(Method method,Anno anno){
        anno.setAnnos(resolver(method.getAnnotations()));
    }
    public static void loadAnnotation(Class clazz,Anno anno){
        anno.setAnnos(resolver(clazz.getAnnotations()));
    }

    public static boolean isSpringMvc(Class clazz,Anno anno) {
        loadAnnotation(clazz,anno);
        return isRequestMapping(anno) || anno.exists(RestController,Controller);
    }

    public static boolean isRequestMapping(Anno anno) {
        return anno.exists(RequestMapping);
    }

    public static <T extends Api> T springMvcSupport(T action, com.daysh.apiface.core.support.spring.RequestMapping anno) {
        action.setMethods(anno.getMethod());
        action.setProduces(anno.getProduces());
        action.setConsumes(anno.getConsumes());
        String name = anno.getName();
        if(StringUtils.isEmpty(name)){
            if(ObjectUtil.isNotEmpty(anno.getValue())){
                name = anno.getValue().get(0);
            } else if(ObjectUtil.isNotEmpty(anno.getPath())){
                name = anno.getPath().get(0);
            }
        }
        if(StringUtils.isNotEmpty(name)){
            action.setUri(name);
        }
        return action;
    }

    public static Action springMvcSupport(MethodMark mark, Action action, Method method, boolean isSpringMvc) {
        loadAnnotation(method,action);
        if (isSpringMvc && isRequestMapping(action)) {
            Parameter[] parameters = method.getParameters();
            List<Param> actionParams = action.getParams();
            for (int i = 0; i < parameters.length; i++) {
                ParamMark paramMark = mark.getParams().get(i);

                for (int j = (actionParams.size() - 1); j > -1; j--) {
                    Param param = actionParams.get(j);
                    if (StringUtils.equals(param.getName(), paramMark.getName())) {
                        Map<String, Annotation> annos = resolver(parameters[i].getAnnotations());
                        BaseElement body = (BaseElement) annos.get(RequestBody.name());
                        BaseElement p = (BaseElement) annos.get(RequestParam.name());
                        BaseElement path = (BaseElement) annos.get(PathVariable.name());
                        if (body != null) {
                            param.setRequired(body.isRequired());
                            param.setForm(ParamEnum.BODY);
                            action.setRequiredBody(body.isRequired());
                            action.setBody(true);
                        }else if (p != null) {
                            param.setForm(ParamEnum.FORM);
                            param.setRequired(p.isRequired());
                            param.setExample(p.getDefaultValue());
                            String name = StringUtils.isNotEmpty(p.getName()) ? p.getName() : p.getValue();
                            if (StringUtils.isNotEmpty(name)) {
                                param.setName(name);
                            }
                        }else if (path != null) {
                            param.setRequired(path.isRequired());
                            param.setForm(ParamEnum.PATH);
                            String name = StringUtils.isNotEmpty(path.getName()) ? path.getName() : path.getValue();
                            if (StringUtils.isNotEmpty(name)) {
                                param.setName(name);
                            }
                        }
                    }
                }
            }
            return springMvcSupport(action, (com.daysh.apiface.core.support.spring.RequestMapping)action.getAnno(RequestMapping));
        }
        return action;
    }

    public static ActionGroup springMvcSupport(ClassMark mark, ActionGroup action, Class clazz, boolean isSpringMvc) {
        if (isSpringMvc && isRequestMapping(action)) {
            return springMvcSupport(action, (com.daysh.apiface.core.support.spring.RequestMapping)action.getAnno(RequestMapping));
        }
        return action;
    }
}
