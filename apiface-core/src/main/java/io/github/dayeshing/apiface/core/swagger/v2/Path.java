package io.github.dayeshing.apiface.core.swagger.v2;

import com.alibaba.fastjson.JSONObject;
import io.github.dayeshing.apiface.core.api.meta.Author;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

import java.util.*;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Path
 * @ClassAllName: io.github.dayeshing.apiface.core.swagger.v2.Path
 * @Author： Daye Shing
 */
public class Path implements JsonApi {

    /**
     * 排除字段
     */
    private List<String> exclude;

    /**
     * 是否过期
     */
    private boolean deprecated;
    /**
     * 作者
     */
    private Author author;
    /**
     * 版本
     */
    private String version;

    /**
     * 卸载全局包装
     */
    private boolean unpack;

    private boolean body;

    private boolean requiredBody;

    private String url;

    private List<String> methods;

    private List<String> tags;

    private String summary;

    private String description;

    private List<String> consumes;

    private List<String> produces;

    private Response responses;

    private List<Parameter> parameters;

    private String error;
    private String date;

    public Path() {
    }

    public Path(String method,String summary, String description) {
        this.summary = method;
        if(ObjectUtil.isNotEmpty(summary)){
            this.summary = String.format("%s(%s)",summary,method);
        }
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public Response getResponses() {
        return responses;
    }

    public void setResponses(Response responses) {
        this.responses = responses;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(getUrl(), path.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        if(ObjectUtil.isEmpty(methods)){
            methods = Arrays.asList("get");
        }
        for (String method : methods) {
            JSONObject path = new JSONObject();
            path.put("tags", tags);
            path.put("summary", summary);
            path.put("date", date);
            path.put("operationId", UUID.randomUUID().toString());
            path.put("unpack", unpack);
            path.put("body", body);
            path.put("requiredContent", requiredBody);
            path.put("deprecated", deprecated);
            path.put("description",description);
            path.put("consumes", ObjectUtil.isNotEmpty(consumes) ? consumes : Arrays.asList("*/*"));
            path.put("produces", ObjectUtil.isNotEmpty(produces) ? produces : Arrays.asList("*/*"));
            if (ObjectUtil.isNotEmpty(exclude)) {
                path.put("excludes", exclude);
            }
            if (author != null) {
                JSONObject user = new JSONObject();
                user.put("name", author.getName());
                user.put("contact", author.getContact());
                path.put("author", user);
            }
            if (ObjectUtil.isNotEmpty(version)) {
                path.put("version", version);
            }

            JSONObject res = new JSONObject();
            for (Status value : Status.values()) {
                JSONObject status = new JSONObject();
                status.put("description", value.getDescription());
                if(value.equals(Status.ERROR)){
                    status.put("error", error);
                }else if (responses != null) {
                    status.put("schema", responses.toJSON());
                }
                res.put(value.getCode(), status);
            }
            path.put("responses", res);

            if (ObjectUtil.isNotEmpty(parameters)) {
                List<JSONObject> ret = new ArrayList<JSONObject>();
                for (Parameter parameter : parameters) {
                    ret.add(parameter.toJSON());
                }
                path.put("parameters", ret);
            }
            json.put(method.toLowerCase(), path);
        }
        return json;
    }

    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isUnpack() {
        return unpack;
    }

    public void setUnpack(boolean unpack) {
        this.unpack = unpack;
    }

    public boolean isBody() {
        return body;
    }

    public void setBody(boolean body) {
        this.body = body;
    }

    public boolean isRequiredBody() {
        return requiredBody;
    }

    public void setRequiredBody(boolean requiredBody) {
        this.requiredBody = requiredBody;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
