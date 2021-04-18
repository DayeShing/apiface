/**
 * @BelongsProject： apiface
 * @BelongsPackage： io.github.dayeshing.apiface.core.meta
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 16:42
 * @Description: <p>  </p>
 */
package io.github.dayeshing.apiface.core.api.meta;

import io.github.dayeshing.apiface.core.bean.Annotation;
import io.github.dayeshing.apiface.core.enums.AnnotationSupportEnum;
import io.github.dayeshing.apiface.core.util.ObjectUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: <p> 接口分组类 </p>
 * @ClassName: Action
 * @ClassAllName: io.github.dayeshing.apiface.core.meta.Action
 * @Author： Daye Shing
 * @Date： 2020-08-15 16:42
 */
public class ActionGroup implements Meta, Api,Anno, Serializable {

    private static final long serialVersionUID = -1L;

    private String name;
    private String desc;

    /**
     * 标签，分组（可以合并业务）
     */
    private List<String> tags;

    /**
     * 是否过期，通过计算获取
     */
    private boolean deprecated;

    /**
     * 访问地址
     */
    private String uri;

    /**
     * 这里用于继承类-可以递归获取继承对象中的字段
     */
    private String ref;

    /**
     * 接口
     */
    private List<Action> actions;

    private List<String> methods;

    private List<String> consumes;

    private List<String> produces;
    /**
     * 作者
     */
    private Author author;
    /**
     * 版本
     */
    private String version;

    /**
     * pack统一包装返回注释
     */
    private String pack;

    private boolean hidden;

    private Map<String, Annotation> annos;

    @Override
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public List<String> getMethods() {
        return methods;
    }

    @Override
    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    @Override
    public List<String> getConsumes() {
        return consumes;
    }

    @Override
    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    @Override
    public List<String> getProduces() {
        return produces;
    }

    @Override
    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    @Override
    public Author getAuthor() {
        return author;
    }

    @Override
    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionGroup that = (ActionGroup) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public Map<String, Annotation> getAnnos() {
        return annos;
    }

    @Override
    public void setAnnos(Map<String, Annotation> annos) {
        this.annos = annos;
    }

    @Override
    public boolean exists(AnnotationSupportEnum ... es) {
        if(es != null && ObjectUtil.isNotEmpty(annos)){
            for (AnnotationSupportEnum e : es) {
                if(annos.containsKey(e.name())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Annotation getAnno(AnnotationSupportEnum e) {
        if(ObjectUtil.isNotEmpty(annos)){
            return annos.get(e.name());
        }
        return null;
    }
}
