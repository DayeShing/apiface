package io.github.dayeshing.apiface.core.api.meta;

import java.util.List;
import java.util.Set;

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

    Set<String> getUri();

    void setUri(Set<String> uri);

    Author getAuthor();

    void setAuthor(Author author);

    String getVersion();

    void setVersion(String version);
}
