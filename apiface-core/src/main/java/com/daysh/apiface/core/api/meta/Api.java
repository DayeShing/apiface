package com.daysh.apiface.core.api.meta;

import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Api
 * @ClassAllName: com.daysh.apiface.core.api.meta.Api
 * @Author： Daye Shing
 */
public interface Api {

    List<String> getMethods();

    void setMethods(List<String> methods);

    List<String> getConsumes();

    void setConsumes(List<String> consumes);

    List<String> getProduces();

    void setProduces(List<String> produces);

    String getUri();

    void setUri(String uri);

    Auther getAuther();

    void setAuther(Auther auther);

    String getVersion();

    void setVersion(String version);
}
