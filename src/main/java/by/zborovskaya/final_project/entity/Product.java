package by.zborovskaya.final_project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Product extends Entity {
    private long id;
    private long category_id;
    @JsonProperty("name")
    private String name;
    private String picturePath;
    private String composition;
    private BigDecimal weight;
    private Integer water_temperature;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime brewing_time;
    private BigDecimal price;
    private boolean active;

    public Product() {
    }

    public Product(long category_id, String name, String picturePath, String composition,
                   BigDecimal weight, Integer water_temperature, LocalTime brewing_time, BigDecimal price, boolean active) {
        this.category_id = category_id;
        this.name = name;
        this.picturePath = picturePath;
        this.composition = composition;
        this.weight = weight;
        this.water_temperature = water_temperature;
        this.brewing_time = brewing_time;
        this.price = price;
        this.active = active;
    }

    public Product(long id, long category_id, String name, String picturePath, String composition,
                   BigDecimal weight, Integer water_temperature, LocalTime brewing_time, BigDecimal price, boolean active) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.picturePath = picturePath;
        this.composition = composition;
        this.weight = weight;
        this.water_temperature = water_temperature;
        this.brewing_time = brewing_time;
        this.price = price;
        this.active = active;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getWater_temperature() {
        return water_temperature;
    }

    public void setWater_temperature(Integer water_temperature) {
        this.water_temperature = water_temperature;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalTime getBrewing_time() {
        return brewing_time;
    }

    public void setBrewing_time(LocalTime brewing_time) {
        this.brewing_time = brewing_time;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", category_id=" + category_id +
                ", name='" + name + '\'' +
                ", picturePath='" + picturePath + '\'' +
                ", composition='" + composition + '\'' +
                ", weight=" + weight +
                ", water_temperature=" + water_temperature +
                ", brewing_time=" + brewing_time +
                ", price=" + price +
                ", is_active=" + active +
                '}';
    }
}
