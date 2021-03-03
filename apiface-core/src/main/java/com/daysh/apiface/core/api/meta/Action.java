/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.meta
 * @Author： Daye Shing
 * @CreateTime： 2020-08-15 16:50
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.api.meta;

import com.daysh.apiface.core.bean.Annotation;
import com.daysh.apiface.core.enums.AnnotationSupportEnum;
import com.daysh.apiface.core.util.ObjectUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: <p> 接口类 </p>
 * @ClassName: Action
 * @ClassAllName: com.daysh.apiface.core.meta.Action
 * @Author： Daye Shing
 * @Date： 2020-08-15 16:50
 */
public class Action implements Meta, Api, Anno, Serializable {

    private static final long serialVersionUID = -11L;

    /**
     * 返回，通过计算或者定义
     */
    private Return ret;

    /**
     * 参数
     */
    private List<Param> params;
    /**
     * 排除字段
     */
    private List<String> exclude;
    /**
     * 方法名称
     */
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 是否过期
     */
    private boolean deprecated;
    /**
     * 作者
     */
    private Auther auther;
    /**
     * 版本
     */
    private String version;

    /**
     * 访问uri
     */
    private String uri;

    private List<String> methods;

    private List<String> consumes;

    private List<String> produces;

    /**
     * 卸载全局包装
     */
    private boolean unpack;

    private boolean body;

    private boolean requiredBody;

    private boolean hidden;

    /**
     * 摘要
     */
    private String summary;

    private Map<String, Annotation> annos;

    public Return getRet() {
        return ret;
    }

    public void setRet(Return ret) {
        this.ret = ret;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
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

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public Auther getAuther() {
        return auther;
    }

    @Override
    public void setAuther(Auther auther) {
        this.auther = auther;
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

    @Override
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isRequiredBody() {
        return requiredBody;
    }

    public void setRequiredBody(boolean requiredBody) {
        this.requiredBody = requiredBody;
    }
    public Map<String, Annotation> getAnnos() {
        return annos;
    }

    @Override
    public void setAnnos(Map<String, Annotation> annos) {
        this.annos = annos;
    }

    @Override
    public boolean exists(AnnotationSupportEnum... es) {
        if(es != null && ObjectUtil.isNotEmpty(annos)){
            for (AnnotationSupportEnum e : es) {
                if(annos.containsKey(e.name())){
                    return true;
                }
            }
        }
        return false;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public Annotation getAnno(AnnotationSupportEnum e) {
        if(ObjectUtil.isNotEmpty(annos)){
            return annos.get(e.name());
        }
        return null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(getUri(), action.getUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri());
    }
}
