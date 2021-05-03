package io.github.dayeshing.apiface.core.model;

import java.util.List;
import java.util.Map;

/**
 * Hi的包装类描述
 * @model 唯一显示名称
 * @author Daye Shing | 896379914@qq.com
 * @since 1.0
 * @date 2021/3/2 15:06
 */
public class Hi {

    /**
     * dsd
     */
    private java.lang.Boolean f;

    /**
     * dfhdf
     */
    private java.util.Map<String,Object> dd;

    /**
     * dfhdf
     */
    private java.util.Map<Hello,Head> ddf;

    /**
     * 获取资源
     */
    private List<Map<String,Head>> res;


    /**
     * 结果
     */
    private Map<String,Head>[] result;

    /**
     * sdsds
     */
    private int ddhh;
    /**
     * dsdsdgdf
     */
    private Hello ddg;
    /**
     * id
     * @required
     */
    private String id;
    /**
     * 名称
     * @example aa
     */
    private String name = "aa";

    public Hi() {
    }

    public Hi(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.lang.Boolean getF() {
        return f;
    }

    public void setF(java.lang.Boolean f) {
        this.f = f;
    }

    public Map<String, Object> getDd() {
        return dd;
    }

    public void setDd(Map<String, Object> dd) {
        this.dd = dd;
    }

    public Map<Hello, Head> getDdf() {
        return ddf;
    }

    public void setDdf(Map<Hello, Head> ddf) {
        this.ddf = ddf;
    }

    public List<Map<String, Head>> getRes() {
        return res;
    }

    public void setRes(List<Map<String, Head>> res) {
        this.res = res;
    }

    public int getDdhh() {
        return ddhh;
    }

    public void setDdhh(int ddhh) {
        this.ddhh = ddhh;
    }

    public Hello getDdg() {
        return ddg;
    }

    public void setDdg(Hello ddg) {
        this.ddg = ddg;
    }

    public Map<String, Head>[] getResult() {
        return result;
    }

    public void setResult(Map<String, Head>[] result) {
        this.result = result;
    }
}
