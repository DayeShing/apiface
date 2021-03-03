//
///**
// * @BelongsProject： apiface
// * @BelongsPackage： com.daysh.apiface.core.api.resolver
// * @Author： Daye Shing
// * @Description: <p>  </p>
// */
//package com.daysh.apiface.core.api.resolver;
//
//import com.alibaba.fastjson.JSON;
//import com.daysh.apiface.core.api.meta.Action;
//import com.daysh.apiface.core.api.meta.ActionGroup;
//import com.daysh.apiface.core.api.meta.Api;
//import com.daysh.apiface.core.api.meta.Param;
//import com.daysh.apiface.core.comment.impl.ClassMark;
//import com.daysh.apiface.core.comment.impl.MethodMark;
//import com.daysh.apiface.core.comment.impl.ParamMark;
//import com.daysh.apiface.core.enums.ParamEnum;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @Description: <p> springmvc 支持，该实现是不能实现加载另一个加载器加载出来的 </p>
// * @ClassName: SpringMvcSupportHelper
// * @ClassAllName: com.daysh.apiface.core.api.resolver.SpringMvcSupportHelper
// * @Author： Daye Shing
// */
//public class SpringMvcSupportHelper {
//
//    public static boolean isSpringMvc(Class clazz) {
//        //AnnotationSupportEnum
//        Annotation[] annotations = clazz.getAnnotations();
//        for (Annotation annotation : annotations) {
//            System.err.println(JSON.toJSONString(annotation));
//            System.err.println(annotation.annotationType().getName());
////
////
////            if (
////                    StringUtils.equals( RestController.class.getName(),annotation.annotationType().getName())
////                    ||
////                StringUtils.equals(Controller.class.getName(),annotation.annotationType().getName())) {
////                return true;
////            }
//        }
//        if (clazz.getAnnotation(RestController.class) != null) {
//            return true;
//        }
//        if (clazz.getAnnotation(Controller.class) != null) {
//            return true;
//        }
//        return false;
//    }
//
//    public static boolean isRequestMapping(Method method) {
//        if (method.getAnnotation(RequestMapping.class) != null) {
//            return true;
//        }
//        return false;
//    }
//
//    public static boolean isRequestMapping(Class clazz) {
//        if (clazz.getAnnotation(RequestMapping.class) != null) {
//            return true;
//        }
//        return false;
//    }
//
//    public static <T extends Api> T springMvcSupport(T action, RequestMapping anno) {
//        RequestMethod[] md = anno.method();
//        List<String> methods = new ArrayList<>();
//        for (RequestMethod m : md) {
//            methods.add(m.name());
//        }
//        if (methods.size() > 0) {
//            action.setMethods(methods);
//        }
//        action.setProduces(Arrays.asList(anno.produces()));
//        action.setConsumes(Arrays.asList(anno.consumes()));
//        String name = anno.name();
//        String[] value = anno.value();
//        name = "".equals(name) ? (value.length == 0 ? anno.path()[0] : value[0]) : name;
//        action.setUri(name);
//        return action;
//    }
//
//    public static Action springMvcSupport(MethodMark mark, Action action, Method method, boolean isSpringMvc) {
//        if (isSpringMvc && isRequestMapping(method)) {
//            List<Param> params = new ArrayList<>();
//            Parameter[] parameters = method.getParameters();
//            List<Param> actionParams = action.getParams();
//            for (int i = 0; i < parameters.length; i++) {
//                ParamMark paramMark = mark.getParams().get(i);
//                RequestBody body = parameters[i].getAnnotation(RequestBody.class);
//                RequestParam p = parameters[i].getAnnotation(RequestParam.class);
//                PathVariable path = parameters[i].getAnnotation(PathVariable.class);
//                for (int j = (actionParams.size() - 1); j > -1; j--) {
//                    Param param = actionParams.get(j);
//                    if (StringUtils.equals(param.getName(), paramMark.getName())) {
//                        if (body != null) {
//                            param.setRequired(body.required());
//                            param.setForm(ParamEnum.BODY);
//                            action.setRequiredBody(body.required());
//                            action.setBody(true);
//                        }
//                        if (p != null) {
//                            param.setForm(ParamEnum.FORM);
//                            param.setRequired(p.required());
//                            param.setExample(p.defaultValue());
//                            String name = StringUtils.isNotEmpty(p.name()) ? p.name() : p.value();
//                            if (StringUtils.isNotEmpty(name)) {
//                                param.setName(name);
//                            }
//                        }
//                        if (path != null) {
//                            param.setRequired(path.required());
//                            param.setForm(ParamEnum.PATH);
//                        }
//                        // 这里校验参数一致性
//                        params.add(param);
//                        actionParams.remove(j);
//                    }
//                }
//            }
//            action.setParams(params);
//            return springMvcSupport(action, method.getAnnotation(RequestMapping.class));
//        }
//        return action;
//    }
//
//    public static ActionGroup springMvcSupport(ClassMark mark, ActionGroup action, Class clazz, boolean isSpringMvc) {
//        if (isSpringMvc && isRequestMapping(clazz)) {
//            return springMvcSupport(action, (RequestMapping) clazz.getAnnotation(RequestMapping.class));
//        }
//        return action;
//    }
//}
