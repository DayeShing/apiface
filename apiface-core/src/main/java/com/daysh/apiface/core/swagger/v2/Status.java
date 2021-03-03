package com.daysh.apiface.core.swagger.v2;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Status
 * @ClassAllName: com.daysh.apiface.core.swagger.v2.Status
 * @Author： Daye Shing
 */
public enum Status {

    OK("200","成功(OK)"),
    ERROR("50x","错误(ERROR)");

    private String code;
    private String description;

    Status(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
