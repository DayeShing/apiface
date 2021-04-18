package io.github.dayeshing.apiface.core.swagger.v2;

import java.util.Objects;

/**
 * @Description: <p> TODO </p>
 * @ClassName: Tags
 * @ClassAllName: io.github.dayeshing.apiface.core.swagger.v2.Tags
 * @Author： Daye Shing
 */
public class Tag {

    private String name;
    private String description;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(getName(), tag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
