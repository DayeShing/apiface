package io.github.dayeshing.apiface.gradle;

import org.gradle.api.Project;

/**
 * ApiExtension的描述
 *
 * @author Daye Shing | 896379914@qq.com
 * @ignore
 * @date 2021-04-21 05:42
 * @since 1.0
 */
public class ApiExtension {

    private boolean gzip = true;

    private boolean api = true;

    private String rule = "";

    private String host = "127.0.0.1:8080";

    private String basePath = "/app";

    private String owner = "apiface © 土豆联盟出品";

    private String license = "9527";

    private String licenseUrl = "http://self.daysh.top";

    private String url = "http://self.daysh.top";

    private String developer = "Daye Shing|xuandeyu14@gmail.com";

    private String description = "apiface让您的接口文档更丰富、更优雅";

    private String version = "1.0.0";

    private String title = "接口文档 APIs";

    public boolean isGzip() {
        return gzip;
    }

    public void setGzip(boolean gzip) {
        this.gzip = gzip;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isApi() {
        return api;
    }

    public void setApi(boolean api) {
        this.api = api;
    }
}
