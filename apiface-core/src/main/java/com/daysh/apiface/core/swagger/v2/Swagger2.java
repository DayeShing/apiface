package com.daysh.apiface.core.swagger.v2;

import com.alibaba.fastjson.JSONObject;
import com.daysh.apiface.core.api.meta.*;
import com.daysh.apiface.core.api.resolver.ApiTransform;
import com.daysh.apiface.core.enums.VariableEnum;
import com.daysh.apiface.core.util.ObjectUtil;

import java.util.*;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Swagger2
 * @ClassAllName: com.daysh.apiface.core.swagger.Swagger2
 * @Author： Daye Shing
 */
public class Swagger2 implements ApiTransform, JsonApi {

    private Set<Tag> tags = new HashSet<Tag>();
    private Set<Path> action = new HashSet<Path>();
    private List<Path> doublePath = new ArrayList<>();

    private List<Definition> definitions = new ArrayList<Definition>();
    private Map<String, ActionGroup> actions;
    private Map<String, FieldGroup> fields;

    @Override
    public JSONObject transform(Map<String, ActionGroup> actions, Map<String, FieldGroup> fields) {
        this.actions = actions;
        this.fields = fields;

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
                if(p.getUrl().equals(path)){
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
                arr.put(property.getName(), property.toJSON());
            }
        }
        json.put("definitions", arr);
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
            ps.add(getPropertie(field));
        }
        return ps;
    }

    protected Propertie getPropertie(Field f) {
        Propertie p = new Propertie();
        p.setName(f.getName());
        p.setDescription(f.getDesc());
        p.setDeprecated(f.isDeprecated());
        p.setFormat(f.getFormat());
        p.setExample(f.getExample());
        p.setRequired(f.isRequired());
        p.setIgnore(f.isIgnore());
        p.setOnlyRead(f.isOnlyRead());
        p.setType(f.getType());
        p.setRef(f.getRef());
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

    protected void getAction(ActionGroup group, Action action) {

        Path path = new Path(action.getName(),action.getSummary(), action.getDesc());
        path.setRequiredBody(action.isRequiredBody());
        path.setBody(action.isBody());
        path.setUnpack(action.isUnpack());
        path.setDeprecated(group.isDeprecated() || action.isDeprecated());
        path.setExclude(action.getExclude());
        if (ObjectUtil.isEmpty(group.getTags())) {
            path.setTags(Arrays.asList(group.getName()));
        } else {
            path.setTags(group.getTags());
        }
        path.setDate(action.getDate());
        path.setError(action.getError());
        path.setAuther(action.getAuther() == null ? group.getAuther() : action.getAuther());
        path.setVersion(ObjectUtil.isEmpty(action.getVersion()) ? group.getVersion() : action.getVersion());
        path.setMethods(ObjectUtil.isEmpty(action.getMethods()) ? group.getMethods() : action.getMethods());
        path.setConsumes(ObjectUtil.isEmpty(action.getConsumes()) ? group.getConsumes() : action.getConsumes());
        path.setProduces(ObjectUtil.isEmpty(action.getProduces()) ? group.getProduces() : action.getProduces());
        path.setUrl(uri(group.getUri(), action.getUri()));
        path.setResponses(getResponse(action.getRet()));
        path.setParameters(getParameters(action.getParams()));
        if(this.action.contains(path)){
            doublePath.add(path);
            return;
        }
        this.action.add(path);
    }

    protected Response getResponse(Return ret) {
        Response response = new Response();
        if (ret == null) {
            response.setType(VariableEnum.VOID.getType());
        }
        response.setDescription(ret.getDesc());
        if (ret.isBase()) {
            // 基本类型
            response.setType(ret.getType());
        } else if (ObjectUtil.equals(ret.getName(), ret.getRef())) {
            //复杂类型非泛型
            if (fields.containsKey(ret.getRef())) {
                //存在该文档
                response.setRef(ret.getRef());
            } else {
                // 不存在定义，返回一个模糊对象
                response.setType(VariableEnum.OBJECT.getType());
            }
        }
        return response;
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
        if (param.isBase()) {
            // 基本类型
            parameter.setType(param.getType());
        } else if (ObjectUtil.equals(param.getType(), param.getRef())) {
            //复杂类型非泛型

            if (fields.containsKey(param.getRef())) {
                //存在该文档
                parameter.setRef(param.getRef());
            } else {
                // 不存在定义，返回一个模糊对象
                parameter.setType(VariableEnum.OBJECT.getType());
            }
        } else {
            //泛型
            parameter.setType(param.getType());
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
