/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.resolver.impl
 * @Author： Daye Shing
 * @CreateTime： 2020-08-16 10:20
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.api.resolver;

import com.alibaba.fastjson.JSON;
import io.github.dayeshing.apiface.core.api.meta.*;
import io.github.dayeshing.apiface.core.api.v2.SpringMvcSupportHelper;
import io.github.dayeshing.apiface.core.comment.Mark;
import io.github.dayeshing.apiface.core.comment.impl.ClassMark;
import io.github.dayeshing.apiface.core.comment.impl.FieldMark;
import io.github.dayeshing.apiface.core.comment.impl.MethodMark;
import io.github.dayeshing.apiface.core.comment.impl.ParamMark;
import io.github.dayeshing.apiface.core.comment.tag.GeneralTag;
import io.github.dayeshing.apiface.core.enums.TagEnum;
import io.github.dayeshing.apiface.core.enums.VariableEnum;
import io.github.dayeshing.apiface.core.exception.ApiException;
import io.github.dayeshing.apiface.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 纯doc文档解析规则为：
 * 接口类：
 *
 * @author 标明开发该类模块的作者
 * // 纯文本作者
 * @author Rod Johnson
 * // 纯文本作者，邮件
 * @author Igor Hersht | igorh@ca.ibm.com
 * @action groupName1, name2, name3, 分组命名
 * @Description: <p> 纯doc接口转换实现，- 后面会有springMvc-或者struck2等mvc的实现 </p>
 * @ClassName: ApiResolverImple
 * @ClassAllName: io.github.dayeshing.apiface.core.resolver.impl.ApiResolverImple
 * @Author： Daye Shing
 * @Date： 2020-08-16 10:20
 * @since 标明该类模块的版本 ( 指定版本)
 */
public class ApiResolverImpl implements ApiResolver, GetFieldGroupResolver {

    protected Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 类加载器-去掉类加载器校验-纯doc解析
     */
    private ClassLoader classLoader;

    /**
     * 接口
     */
    private Map<String, ActionGroup> actions;

    /**
     * model
     */
    private Map<String, FieldGroup> fields;

    public ApiResolverImpl() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ApiResolverImpl(ClassLoader classLoader) {
        this.classLoader = classLoader;
        actions = new HashMap<String, ActionGroup>(100);
        fields = new HashMap<String, FieldGroup>(100);
    }

    @Override
    public void resolver(List<ClassMark> marks) {
        if (ObjectUtil.isNotEmpty(marks)) {
            //遍历类文档
            for (ClassMark mark : marks) {
                Map<String, List<String>> tags = getTagValues(mark.getDocTags());

                // 设置描述和类全名
                String name = mark.getName();
                try {
                    Class clazz = Class.forName(name, false, classLoader);
                    Annotation annotation = clazz.getAnnotation(Deprecated.class);
                    String desc = mark.getDesc();
                    if (TagEnum.isAction(tags)) {
                        ActionGroup group = new ActionGroup();
                        group.setDeprecated(annotation != null);
                        group.setName(name);
                        group.setDesc(desc);
                        group.setRef(ref(clazz));
                        boolean springMvc = SpringMvcSupportHelper.isSpringMvc(clazz, group);
                        List<Action> apis = new ArrayList<Action>();
                        for (MethodMark method : mark.getMethods()) {
                            Method md = associateMethod(clazz, method);
                            if (md == null) {
                                continue;
                            }
                            apis.add(getAction(method, md, springMvc));
                        }
                        group.setActions(apis);
                        actions.put(name, SpringMvcSupportHelper.springMvcSupport(mark, transTag(group, tags), clazz, springMvc));
                    }
                    if (TagEnum.isModel(tags)) {

//                        Type genericSuperclass = clazz.getGenericSuperclass();
//                        genericSuperclass.getTypeName();
//                        继承泛型类

                        FieldGroup group = new FieldGroup();
                        group.setDeprecated(annotation != null);
                        group.setName(name);
                        group.setDesc(desc);
                        group.setTag((String) TagEnum.MODEL.resolver(tags));
                        group.setRef(refs(clazz));
                        group.setFields(fields(clazz, mark));
                        fields.put(name, group);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected <T extends Api> T transTag(T action, Map<String, List<String>> tags) {
        for (Map.Entry<String, List<String>> entry : tags.entrySet()) {
            TagEnum tag = TagEnum.nameOf(entry.getKey());
            List<String> value = entry.getValue();
            switch (tag) {
                case URI:
                    HashSet<String> url = new HashSet<>();
                    url.addAll((List<String>) tag.resolver(value));
                    action.setUri(url);
                    break;
                case METHOD:
                    action.setMethods((List<String>) tag.resolver(value));
                    break;
                case PRODUCE:
                    action.setProduces((List<String>) tag.resolver(value));
                    break;
                case CONSUME:
                    action.setConsumes((List<String>) tag.resolver(value));
                    break;
                case AUTHOR:
                    action.setAuthor((Author) tag.resolver(value));
                    break;
                case SINCE:
                    action.setVersion((String) tag.resolver(value));
                    break;
                case EXCLUDE:
                    if (action instanceof Action) {
                        ((Action) action).setExclude((List<String>) tag.resolver(value));
                    }
                    break;
                case SUMMARY:
                    if (action instanceof Action) {
                        ((Action) action).setSummary((String) tag.resolver(value));
                    }
                    break;
                case HIDDEN:
                    if (action instanceof Action) {
                        // 是否隐藏
                        ((Action) action).setHidden(true);
                    }
                    if (action instanceof ActionGroup) {
                        // 是否隐藏
                        ((ActionGroup) action).setHidden(true);
                    }
                    break;
                case RETURN:
                    if (action instanceof Action) {
                        // 这里要校验返回值
                        ((Action) action).setRet(validReturn((Return) tag.resolver(value)));
                    }
                    break;
                case PARAM:
                    if (action instanceof Action) {
                        // 这里要校验返回值
                        ((Action) action).setParams(validParam((List<Param>) tag.resolver(value)));
                    }
                    break;
                case UNPACK:
                    if (action instanceof Action) {
                        ((Action) action).setUnpack(true);
                    }
                    break;
                case ERROR:
                    if (action instanceof Action) {
                        ((Action) action).setError((String) tag.resolver(value));
                    }
                    break;
                case DATE:
                    if (action instanceof Action) {
                        ((Action) action).setDate((String) tag.resolver(value));
                    }
                    break;
                case ACTION:
                    if (action instanceof ActionGroup) {
                        ((ActionGroup) action).setTags((List<String>) tag.resolver(value));
                    }
                    break;
                case PACK:
                    if (action instanceof ActionGroup) {
                        ((ActionGroup) action).setPack(validClass((String) tag.resolver(value)));
                    }
                    break;
            }
        }
        return action;
    }

    protected List<Param> validParam(List<Param> params) {
        List<Param> parameters = new ArrayList<Param>();
        if (ObjectUtil.isNotEmpty(params)) {
            for (Param param : params) {

                String ref = param.getType();

                VariableEnum of = VariableEnum.of(ref);
                String array = null;
                String type = ref;
                param.setType(of.getType());
                if (of.isBase()) {
                    if (param.isRequired() && ObjectUtil.isEmpty(param.getExample())) {
                        param.setExample(of.getExample());
                    }
                    param.setFormat(of.getFormat());
                    param.setBase(true);
                } else if (of.isArray()) {
                    if (ref.endsWith("[]")) {
                        array = ref.replace("[]", "");
                    } else if (ref.startsWith("array[")) {
                        array = ref.replace("array[", "").replace("]", "");
                    } else if (ref.startsWith("Array[")) {
                        array = ref.replace("Array[", "").replace("]", "");
                    }
                } else if (of.isList()) {
                    int index = type.indexOf("<");
                    if (index > -1) {
                        array = ref.substring(index).replace(">", "").replace("<", "");
                    }
                } else {
                    int index = type.indexOf("<");
                    if (index > -1) {
                        // 这里是泛型属性，
                        param.setGeneric(true);
                        type = type.substring(0, index);
                    }
                    try {
                        param.setRef(Class.forName(type, false, classLoader).getName());
                    } catch (ClassNotFoundException e) {
                        param.setType(VariableEnum.OBJECT.getType());
                        param.setDesc("无法实例:" + param.getType());
                        log.error("无法实例对象:{}:{}", type, param.getType());
                        param.setGeneric(false);
                    }
                }

                if (array != null) {
                    param.setArray(true);
                    of = VariableEnum.of(array);
                    param.setType(of.getType());
                    if (of.isBase()) {
                        if (param.isRequired() && ObjectUtil.isEmpty(param.getExample())) {
                            param.setExample(of.getExample());
                        }
                        param.setFormat(of.getFormat());
                        param.setBase(true);
                    } else {

                        int index = array.indexOf("<");
                        if (index > -1) {
                            // 这里是泛型属性，
                            param.setGeneric(true);
                            array = array.substring(0, index);
                        }
                        try {
                            param.setRef(Class.forName(array, false, classLoader).getName());
                        } catch (ClassNotFoundException e) {
                            param.setType(VariableEnum.OBJECT.getType());
                            param.setRef(VariableEnum.OBJECT.getClazz());
                            param.setDesc("无法实例:" + param.getType());
                            log.error("无法实例对象:{}:{}", array, param.getType());
                            param.setGeneric(false);
                        }
                    }
                }
                parameters.add(param);
            }
        }
        return parameters;
    }

    protected String validClass(String ret) {
        try {
            String name = ret;
            int of = ret.indexOf("<");
            if (of > -1) {
                name = ret.substring(0, of);
            }
            Class.forName(name, false, classLoader);
            return ret;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    protected Return validReturn(Return ret) {
        String ref = ret.getName();

        VariableEnum of = VariableEnum.of(ref);
        String array = null;
        String type = ref;
        ret.setType(of.getType());
        if (of.isBase()) {
            ret.setFormat(of.getFormat());
            ret.setBase(true);
        } else if (of.isArray()) {
            if (ref.endsWith("[]")) {
                array = ref.replace("[]", "");
            } else if (ref.startsWith("array[")) {
                array = ref.replace("array[", "").replace("]", "");
            } else if (ref.startsWith("Array[")) {
                array = ref.replace("Array[", "").replace("]", "");
            }
        } else if (of.isList()) {
            int index = type.indexOf("<");
            if (index > -1) {
                array = ref.substring(index).replace(">", "").replace("<", "");
            }
        } else {
            int index = type.indexOf("<");
            if (index > -1) {
                // 这里是泛型属性，
                ret.setGeneric(true);
                type = type.substring(0, index);
            }
            try {
                ret.setRef(Class.forName(type, false, classLoader).getName());
            } catch (ClassNotFoundException e) {
                ret = new Return(VariableEnum.OBJECT.getClazz(), "无法实例:" + ret.getName());
                ret.setType(VariableEnum.OBJECT.getType());
                log.error("无法实例对象:{}:{}", type, ret.getName());
                ret.setGeneric(false);
                return ret;
            }
        }

        if (array != null) {
            ret.setArray(true);
            of = VariableEnum.of(array);
            ret.setType(of.getType());
            if (of.isBase()) {
                ret.setFormat(of.getFormat());
                ret.setBase(true);
            } else {

                int index = array.indexOf("<");
                if (index > -1) {
                    // 这里是泛型属性，
                    ret.setGeneric(true);
                    array = array.substring(0, index);
                }
                try {
                    ret.setRef(Class.forName(array, false, classLoader).getName());
                } catch (ClassNotFoundException e) {
                    ret = new Return(VariableEnum.OBJECT.getClazz(), "无法实例:" + ret.getName());
                    ret.setType(VariableEnum.OBJECT.getType());
                    log.error("无法实例对象:{}:{}", array, ret.getName());
                    return ret;
                }
            }
        }
        return ret;
    }

    protected Action getAction(MethodMark mark, Method method, boolean isSpringMvc) {
        Action action = new Action();
        action.setName(mark.getName());
        action.setDesc(mark.getDesc());
        action.setDeprecated(method.getAnnotation(Deprecated.class) != null);
        return SpringMvcSupportHelper.springMvcSupport(mark, transTag(action, getTagValues(mark.getDocTags())), method, isSpringMvc);
    }

    protected Method associateMethod(Class clazz, MethodMark method) {
        int paramSize = 0;
        if (method.getParams() != null) {
            paramSize = method.getParams().size();
        }
        Method[] mds = clazz.getMethods();
        int index = 0;
        for (Method md : mds) {
            int count = md.getParameterCount();
            if (md.getName().equals(method.getName()) && count == paramSize && paramSize == index) {
                return md;
            }

            // 方法重写
            if (md.getName().equals(method.getName()) && count == paramSize) {
                List<ParamMark> params = method.getParams();
                Class<?>[] parameterTypes = md.getParameterTypes();
                for (int i = 0; i < paramSize; i++) {
                    String type = params.get(i).getType();
                    int of = type.indexOf("<");
                    if (of > -1) {
                        type = type.substring(0, of);
                    }
                    if (parameterTypes[i].getName().endsWith(type)) {
                        index++;
                    }
                }
                if (paramSize == index) {
                    return md;
                }
            }
        }
        log.error("没有相关的方法:{}:{}", clazz.getName(), method.getName());
        throw new ApiException(String.format("【%s】 no such a method 【%s】 with param 【%s】", clazz.getName(), method.getName(), JSON.toJSONString(method.getParams())));
    }

    protected String realName(Method method) {
        String name = method.getName().replace("get", "").replace("is", "");
        return String.format("%s%s", name.substring(0, 1).toLowerCase(), name.substring(1));
    }

    protected Field getField(Field field, Mark mark) {
        if (mark == null) {
            return field;
        }
        Map<String, List<String>> tags = getTagValues(mark.getDocTags());
        for (Map.Entry<String, List<String>> entry : tags.entrySet()) {
            TagEnum tag = TagEnum.nameOf(entry.getKey());
            List<String> value = entry.getValue();
            switch (tag) {
                case HIDDEN:
                    field.setIgnore(true);
                    break;
                case REQUIRED:
                    field.setRequired(true);
                    break;
                case FORMAT:
                    field.setFormat((String) tag.resolver(value));
                    break;
                case EXAMPLE:
                    field.setExample((String) tag.resolver(value));
                    break;
            }
        }
        return field;
    }

    /**
     * get方法的文档注释
     *
     * @param clazz  |类
     * @param method |反射方法
     * @param mark   |方法描述
     * @param p      |属性描述
     * @return io.github.dayeshing.apiface.core.api.meta.Field|描述
     * @summary get方法的文档注释
     * @method post, get
     * @ignore
     * @author Daye Shing | 896379914@qq.com
     * @since 1.0
     */
    protected Field getField(Class clazz, Method method, MethodMark mark, PropertyDescriptor p) {
        try {
            Field field = new Field();
            field.setName(realName(method));
            field.setDesc(method.getName());
            if (mark != null) {
                field.setDesc(mark.getDesc());
            }
            //简单返回类
            Class<?> type = method.getReturnType();
            // 泛型返回类
            Type genericType = method.getGenericReturnType();
            if (!ObjectUtil.equals(type.getName(), genericType.getTypeName())) {
                log.info("包含泛型:{}:{}", genericType.getTypeName(), genericType.getClass().getName());
                field.setGeneric(true);
            }

            VariableEnum of = VariableEnum.typeof(type);

            field.setType(of.getType());
            if (of.isBase()) {
                field.setType(of.getType());
                field.setBase(true);
                field.setFormat(of.getFormat());
            } else if (of.isArray()) {
//            GenericArrayType
//            System.err.println(type.getTypeName());
//            Type gt = method.getGenericReturnType();
//            System.err.println(gt.getTypeName());
//            System.err.println(gt.getClass().getName());
                field.setArray(true);
                String array = type.getTypeName().replace("[]", "");
                of = VariableEnum.of(array);
                if (!of.isBase()) {
                    field.setType(VariableEnum.OBJECT.getType());
                    field.setRef(array);
                }
            } else if (of.isList()) {
                field.setArray(true);
                Class actualType;
                Type gte = ((ParameterizedType) genericType).getActualTypeArguments()[0];
                // 泛型中包含泛型
                if (gte instanceof ParameterizedType) {
                    actualType = (Class) ((ParameterizedType) gte).getRawType();
//                Type[] types = ((ParameterizedType) gte).getActualTypeArguments();
//                for (Type actualTypeArgument : types) {
//
//                    System.err.println(actualTypeArgument.getTypeName());
//                    System.err.println(actualTypeArgument.getClass().getName());
//                }
                } else {
//                    System.err.println(method);
                    actualType = (Class) gte;
                    // TODO: 2021/5/9 这里未知泛型有错
                    field.setGeneric(false);
                }

                of = VariableEnum.typeof(actualType);
                if (!of.isBase()) {
                    field.setType(VariableEnum.OBJECT.getType());
                    field.setRef(actualType.getTypeName());
                }
            } else {
                field.setRef(type.getName());
            }
            if (field.isArray()) {
                if (of.isBase()) {
                    field.setType(of.getType());
                    field.setBase(true);
                    field.setFormat(of.getFormat());
                }
            }
            field.setDeprecated(method.getAnnotation(Deprecated.class) != null);
            field.setOnlyRead(p.getWriteMethod() == null);
            return getField(field, mark);

        } catch (Exception e) {
            log.error("反射获取方法失败", e);
        }
        Field field = new Field();
        field.setName(realName(method));
        if(mark!=null){
            field.setDesc(mark.getDesc());
        }
        field.setType(VariableEnum.OBJECT.getType());
        return field;
    }

    /**
     * 根据字段属性生成feild
     *
     * @param clazz | 参数的描述 | 必须true/false | 的默认值
     * @param mark  | 参数的描述 | 必须true/false | 的默认值
     * @return io.github.dayeshing.apiface.core.api.meta.Field|描述
     * @exclude 排除参数1, 排除参数2
     * @author Daye Shing
     * @since 1.0
     */
    protected Field getField(Class clazz, FieldMark mark, PropertyDescriptor p) {
        try {
            java.lang.reflect.Field f = clazz.getDeclaredField(mark.getName());
            if (f != null) {
                Field field = new Field();
                field.setName(f.getName());
                field.setDesc(mark.getDesc());
                field.setExample(mark.getExample());
                Class<?> type = f.getType();
                Type genericType = f.getGenericType();
                if (!ObjectUtil.equals(type.getName(), genericType.getTypeName())) {
                    log.info("包含泛型:{}:{}", genericType.getTypeName(), genericType.getClass().getName());
                    field.setGeneric(true);
                }
                VariableEnum of = VariableEnum.typeof(type);
                field.setType(of.getType());
                if (of.isBase()) {
                    field.setBase(true);
                    field.setFormat(of.getFormat());
                } else if (of.isArray()) {
                    field.setArray(true);
                    String array = type.getTypeName().replace("[]", "");
                    of = VariableEnum.of(array);
                    if (!of.isBase()) {
                        field.setType(VariableEnum.OBJECT.getType());
                        field.setRef(array);
                    }
                } else if (of.isList()) {
                    field.setArray(true);
                    Class actualType;
                    Type gte = ((ParameterizedType) genericType).getActualTypeArguments()[0];
                    // 泛型中包含泛型
                    if (gte instanceof ParameterizedType) {
                        actualType = (Class) ((ParameterizedType) gte).getRawType();
                    } else {
                        actualType = (Class) gte;
                        field.setGeneric(false);
                    }
                    of = VariableEnum.typeof(actualType);
                    if (!of.isBase()) {
                        field.setType(VariableEnum.OBJECT.getType());
                        field.setRef(actualType.getTypeName());
                    }
                } else {
                    field.setRef(type.getName());
                }
                if (field.isArray()) {
                    if (of.isBase()) {
                        field.setType(of.getType());
                        field.setBase(true);
                        field.setFormat(of.getFormat());
                    }
                }
                field.setDeprecated(f.getAnnotation(Deprecated.class) != null);
                field.setOnlyRead(p.getWriteMethod() == null);
                return getField(field, mark);
            }
        } catch (Exception e) {
            log.error("反射获取属性失败", e);
        }
        Field field = new Field();
        field.setName(mark.getName());
        field.setDesc(mark.getDesc());
        field.setExample(mark.getExample());
        field.setType(VariableEnum.OBJECT.getType());
        return field;
    }

    protected List<Field> fields(Class clazz, ClassMark mark) {
        List<Field> fields = new ArrayList<>();

        // 填充文档数据
        Map<String, Mark> marks = new HashMap();
        if (ObjectUtil.isNotEmpty(mark.getFields())) {
            for (FieldMark fieldMark : mark.getFields()) {
                marks.put(String.format("get%s%s", fieldMark.getName().substring(0, 1).toUpperCase(), fieldMark.getName().substring(1)), fieldMark);
            }
        }
        if (ObjectUtil.isNotEmpty(mark.getMethods())) {
            for (MethodMark fieldMark : mark.getMethods()) {
                marks.put(fieldMark.getName(), fieldMark);
            }
        }
        try {
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] ps = info.getPropertyDescriptors();
            for (PropertyDescriptor p : ps) {
                Method method = p.getReadMethod();
                if (method == null) {
                    continue;
                }
                String name = method.getName();
                if ("getClass".equals(name)) {
                    continue;
                }
                if (name.startsWith("is")) {
                    name = name.replace("is", "get");
                }
                if (marks.containsKey(name)) {
                    Mark meta = marks.get(name);
                    //文档注释写到字段上
                    if (meta instanceof FieldMark) {
                        fields.add(getField(clazz, (FieldMark) meta, p));
                    }
                    //文档写到get方法上
                    if (meta instanceof MethodMark) {
                        fields.add(getField(null, method, (MethodMark) meta, p));
                    }
                    continue;
                }
                //没有写文档注释的get方法
                fields.add(getField(clazz, method, null, p));
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return fields;
    }

    protected String ref(Class clazz) {
        if (clazz.isInterface()) {
            return null;
        }
        Class superclass = clazz.getSuperclass();
        if (superclass != null && !superclass.equals(Object.class)) {
            return superclass.getName();
        }
        return null;
    }

    protected List<String> refs(Class clazz) {
        List<String> as = new ArrayList<>();
        if (clazz != null) {
            Class[] interfaces = clazz.getInterfaces();
            for (Class anInterface : interfaces) {
                as.add(anInterface.getName());
            }
            String superclass = ref(clazz);
            if (superclass != null) {
                as.add(superclass);
            }
        }
        return as;
    }

    protected Map<String, List<String>> getTagValues(List<GeneralTag> tags) {
        Map<String, List<String>> ret = new HashMap<>();
        for (GeneralTag tag : tags) {
            List<String> values = ret.get(tag.getTagName());
            if (values == null) {
                values = new ArrayList<>();
            }
            values.add(tag.getValue());
            ret.put(tag.getTagName(), values);
        }
        return ret;
    }

    @Override
    public Map<String, ActionGroup> getActions() {
        return actions;
    }

    @Override
    public Map<String, FieldGroup> getFields() {
        return fields;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public FieldGroup getField(String ref) {
        try {
            Class clazz = Class.forName(ref, false, classLoader);
            if (exclude(ref, clazz)) {
                return null;
            }
            BeanInfo info = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] ps = info.getPropertyDescriptors();
            FieldGroup group = new FieldGroup();
            group.setDeprecated(clazz.getAnnotation(Deprecated.class) != null);
            group.setName(ref);
            group.setDesc(clazz.getSimpleName());
            group.setTag(ref);
            group.setRef(refs(clazz));
            List<Field> fields = new ArrayList<>();
            for (PropertyDescriptor p : ps) {
                Method method = p.getReadMethod();
                String name = method.getName();
                if ("getClass".equals(name)) {
                    continue;
                }
                //没有写文档注释的get方法
                fields.add(getField(clazz, method, null, p));
            }
            group.setFields(fields);
            return group;
        } catch (Exception e) {
            log.error("加载无文档注释MODEL错误", e);
        }
        return null;
    }

    @Override
    public boolean exclude(String ref, Class clazz) {
        if (ref != null && ref.startsWith("java.lang.")) {
            return true;
        }
        if ("object".equals(ref)) {
            return true;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return true;
        }
        log.info("加载无文档注释MODEL：{}", ref);
        return false;
    }
}
