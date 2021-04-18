package io.github.dayeshing.apiface.core.api.meta;

import java.util.List;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Api
 * @ClassAllName: io.github.dayeshing.apiface.core.api.meta.Api
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

    Author getAuthor();

    void setAuthor(Author author);

    String getVersion();

    void setVersion(String version);
}
