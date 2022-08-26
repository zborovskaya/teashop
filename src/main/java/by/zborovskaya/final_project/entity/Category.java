package by.zborovskaya.final_project.entity;

import java.util.Objects;

public class Category extends Entity{
    private long id;
    private String categoryName;
    private boolean active;

    public Category() {
    }

    public Category(long id, String categoryName, boolean active) {
        this.id = id;
        this.categoryName = categoryName;
        this.active = active;
    }
    public Category(String categoryName, boolean active) {
        this.categoryName = categoryName;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id && active == category.active && Objects.equals(categoryName, category.categoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryName, active);
    }
}
