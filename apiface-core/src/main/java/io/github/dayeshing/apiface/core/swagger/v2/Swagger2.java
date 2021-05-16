package io.github.dayeshing.apiface.core.swagger.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.github.dayeshing.apiface.core.api.meta.*;
import io.github.dayeshing.apiface.core.api.resolver.ApiTransform;
import io.github.dayeshing.apiface.core.api.resolver.GetFieldGroupResolver;
import io.github.dayeshing.apiface.core.enums.VariableEnum;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

import java.util.*;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Swagger2
 * @ClassAllName: io.github.dayeshing.apiface.core.swagger.Swagger2
 * @Author： Daye Shing
 */
public class Swagger2 implements ApiTransform, JsonApi {

    private Set<Tag> tags = new HashSet<Tag>();
    private Set<Path> action = new HashSet<Path>();
    private List<Path> doublePath = new ArrayList<Path>();

    private List<Definition> definitions = new ArrayList<Definition>(200);
    private Set<String> useRefs = new HashSet<String>();
    private Map<String, ActionGroup> actions = new HashMap<>(200);
    private Map<String, FieldGroup> fields = new HashMap<>(200);

    private GetFieldGroupResolver resolver;

    public Swagger2(GetFieldGroupResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public JSONObject transform(Map<String, ActionGroup> actions, Map<String, FieldGroup> fields) {
        this.actions.putAll(actions);
        this.fields.putAll(fields);

        for (Map.Entry<String, ActionGroup> entry : actions.entrySet()) {
            ActionGroup group = entry.getValue();
            if(group.isHidden()){
                continue;
            }
            List<String> tags = group.getTags();
            if (ObjectUtil.isNotEmpty(tags)) {
                for (String tag : tags) {
                    this.tags.add(new Tag(tag, group.getDesc()));
                }
            } else {
                this.tags.add(new Tag(group.getName(), group.getDesc()));
            }
            getChildActions(group, group);
            getActions(group, group);
        }
        for (Map.Entry<String, FieldGroup> entry : fields.entrySet()) {
            FieldGroup group = entry.getValue();
            if(group.isHidden()){
                continue;
            }
            definitions.add(getDefinition(group));
        }

        return toJSON();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("tags", tags);
        JSONObject as = new JSONObject();
        for (Path path : action) {
            JSONObject jsonPath = path.toJSON();
            for (Path p : doublePath) {
                if(p.getUrl().equals(path.getUrl())){
                    JSONObject tmp = p.toJSON();
                    for (Map.Entry<String, Object> entry : tmp.entrySet()) {
                        jsonPath.put(entry.getKey(),entry.getValue());
                    }
                }

            }
            as.put(path.getUrl(), jsonPath);
        }
        json.put("paths", as);
        JSONObject arr = new JSONObject();
        if (ObjectUtil.isNotEmpty(definitions)) {
            for (Definition property : definitions) {
                if(useRefs.contains(property.getName())){
                    arr.put(property.getName(), property.toJSON());
                }
            }
        }

        json.put("definitions", arr);
        json.put("apiface", "1");
        return json;
    }

    protected Definition getDefinition(FieldGroup group) {
        Definition definition = new Definition();
        definition.setDeprecated(group.isDeprecated());
        definition.setName(group.getName());
        definition.setDescription(group.getDesc());
        definition.setTag(group.getTag());
        // 这里只有两种可能 arr,object
        definition.setType(VariableEnum.OBJECT.getType());

        Set<Propertie> ps = new HashSet<Propertie>();
        ps.addAll(getChildProperties(group));
        ps.addAll(getProperties(group));
        definition.setProperties(ps);
        return definition;
    }

    protected Set<Propertie> getChildProperties(FieldGroup group) {
        Set<Propertie> ps = new HashSet<Propertie>();
        // 这里应该没问题，文档已经存在，只是想去他的继承类再找描述
        for (String ref : group.getRef()) {
            FieldGroup child = fields.get(ref);
            if (child != null) {
                ps.addAll(getChildProperties(child));
                ps.addAll(getProperties(child));
            }
        }
        return ps;
    }

    protected Set<Propertie> getProperties(FieldGroup group) {
        Set<Propertie> ps = new HashSet<Propertie>();
        List<Field> fields = group.getFields();
        for (Field field : fields) {
            if(field != null){
                if(field.isIgnore() || field.isHidden()){
                    continue;
                }
                ps.add(getPropertie(field));
            }
        }
        return ps;
    }

    protected Propertie getPropertie(Field f) {
        Propertie p = new Propertie();
        p.setName(f.getName());
        p.setArray(f.isArray());
        p.setDescription(f.getDesc());
        p.setDeprecated(f.isDeprecated());
        p.setFormat(f.getFormat());
        p.setExample(f.getExample());
        p.setRequired(f.isRequired());
        p.setIgnore(f.isIgnore());
        p.setOnlyRead(f.isOnlyRead());
        p.setType(f.getType());
        // 这里有问题
//        p.setRef(f.getRef());
//      修复后，先去看看有没有该引用，没有将创建一个
        String ref = getRefs(f.getRef());
        p.setRef(ref);
        return p;
    }

    protected void getChildActions(ActionGroup top, ActionGroup group) {
        ActionGroup child = actions.get(group.getRef());
        if (child != null) {
            // 继承包装类与卸载包装类
            String pack = child.getPack();
            String topPack = top.getPack();
            if(ObjectUtil.isNotEmpty(pack) && ObjectUtil.isEmpty(topPack)){
                top.setPack(pack);
            }
            getChildActions(top, child);
            getActions(top, child);
        }
    }

    protected void getActions(ActionGroup top, ActionGroup group) {
        List<Action> actions = group.getActions();
        for (Action action : actions) {
            if(action.isHidden()){
                continue;
            }
            getAction(top, action);
        }
    }
    protected void getAction(ActionGroup group, Action action,String uri){
        Path path = new Path(action.getName(),action.getSummary(), action.getDesc());
        path.setRequiredBody(action.isRequiredBody());
        path.setBody(action.isBody());
        path.setUnpack(action.isUnpack());
        path.setDeprecated(group.isDeprecated() || action.isDeprecated());
        path.setExclude(action.getExclude());
        path.setPack(getRefs(group.getPack()));
        if (ObjectUtil.isEmpty(group.getTags())) {
            path.setTags(Arrays.asList(group.getName()));
        } else {
            path.setTags(group.getTags());
        }
        path.setDate(action.getDate());
        path.setError(action.getError());
        path.setAuthor(action.getAuthor() == null ? group.getAuthor() : action.getAuthor());
        path.setVersion(ObjectUtil.isEmpty(action.getVersion()) ? group.getVersion() : action.getVersion());
        path.setMethods(ObjectUtil.isEmpty(action.getMethods()) ? group.getMethods() : action.getMethods());
        path.setConsumes(ObjectUtil.isEmpty(action.getConsumes()) ? group.getConsumes() : action.getConsumes());
        path.setProduces(ObjectUtil.isEmpty(action.getProduces()) ? group.getProduces() : action.getProduces());
        path.setUrl(uri);
        path.setResponses(getResponse(action.getRet()));
        path.setParameters(getParameters(action.getParams()));
        if(this.action.contains(path)){
            doublePath.add(path);
            return;
        }
        this.action.add(path);
    }
    protected void getAction(ActionGroup group, Action action) {
        Set<String> actionUri = action.getUri();
        if(ObjectUtil.isNotEmpty(actionUri)){
            for (String au : actionUri) {
                Set<String> uri = group.getUri();
                if(ObjectUtil.isNotEmpty(uri)){
                    for (String groupUri : uri) {
                        getAction(group,action,uri(groupUri,au));
                    }
                    continue;
                }
                getAction(group,action,au);
            }
        }
    }

    protected Response getResponse(Return ret) {
        Response response = new Response();
        if (ret == null) {
            response.setType(VariableEnum.VOID.getType());
        }
        response.setArray(ret.isArray());
        response.setType(ret.getType());
        response.setDescription(ret.getDesc());

        //ObjectUtil.equals(ret.getName(), ret.getRef())
//        if (false) {
//            //泛型
////            parameter.setType(param.getType());
//        }
        String ref = getRefs(ret.getRef());
        response.setRef(ref);
        if(ref != null){
            response.setType(VariableEnum.OBJECT.getType());
        }
        return response;
    }

    protected String getRefs(String ref){
        if(ObjectUtil.isNotEmpty(ref)){
            //存在该文档
            if (fields.containsKey(ref)) {
                useRefs.add(ref);
                return ref;
            }
            // 不存在将构建
            FieldGroup group = resolver.getField(ref);
            if(group != null){
                fields.put(ref,group);
                definitions.add(getDefinition(group));
                useRefs.add(ref);
                return ref;
            }
        }
        return null;
    }

    protected List<Parameter> getParameters(List<Param> params) {
        List<Parameter> ps = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(params)) {
            for (Param param : params) {
                ps.add(getParameter(param));
            }
        }
        return ps;
    }

    protected Parameter getParameter(Param param) {
        Parameter parameter = new Parameter();
        parameter.setExample(param.getExample());
        parameter.setIn(param.getForm());
        parameter.setDescription(param.getDesc());
        parameter.setFormat(param.getFormat());
        parameter.setName(param.getName());
        parameter.setRequired(param.isRequired());
        parameter.setArray(param.isArray());
        parameter.setType(param.getType());

        //ObjectUtil.equals(param.getType(), param.getRef()) || param.isArray()
//        if (false) {
//            //泛型
//            parameter.setType(param.getType());
//        }

        String ref = getRefs(param.getRef());
        parameter.setRef(ref);
        if(ref != null){
            parameter.setType(VariableEnum.OBJECT.getType());
        }
        return parameter;
    }

    protected String uri(String prefix, String suffix) {
        StringBuilder url = new StringBuilder();
        if (ObjectUtil.isNotEmpty(prefix)) {
            if (!prefix.startsWith("/")) {
                url.append("/");
            }
            url.append(prefix);
        }
        if (ObjectUtil.isNotEmpty(suffix)) {
            if (!suffix.startsWith("/")) {
                url.append("/");
            }
            url.append(suffix);
        }
        return url.toString();
    }

}
