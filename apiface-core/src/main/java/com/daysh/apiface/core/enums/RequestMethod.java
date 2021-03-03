/**
 * @BelongsProject： apiface
 * @BelongsPackage： com.daysh.apiface.core.enums
 * @Author： Daye Shing
 * @CreateTime： 2021-03-02 03:21
 * @Description: <p>  </p>
 */
package com.daysh.apiface.core.enums;

/**
 * @Description: <p> TODO </p>
 * @ClassName: RequestMethod
 * @ClassAllName: com.daysh.apiface.core.enums.RequestMethod
 * @Author： Daye Shing
 */
public enum  RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private RequestMethod() {
    }
}
