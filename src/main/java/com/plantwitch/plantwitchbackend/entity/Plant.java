package com.plantwitch.plantwitchbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "plants")

public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "waterDate", nullable = false)
    private String waterDate;

    @Column(name = "repotDate", nullable = false)
    private String repotDate;

    @Column(name = "waterInterval", nullable = false)
    private int waterInterval;

    @Column(name = "repotInterval", nullable = false)
    private int repotInterval;

    // Mapping the column of this table
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Plant() {

    }

    public Plant(int repotInterval, int waterInterval, String repotDate, String waterDate, String description, String image, String name, Long id) {
        this.repotInterval = repotInterval;
        this.waterInterval = waterInterval;
        this.repotDate = repotDate;
        this.waterDate = waterDate;
        this.description = description;
        this.image = image;
        this.name = name;
        this.id = id;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWaterDate() {
        return waterDate;
    }

    public void setWaterDate(String waterDate) {
        this.waterDate = waterDate;
    }

    public String getRepotDate() {
        return repotDate;
    }

    public void setRepotDate(String repotDate) {
        this.repotDate = repotDate;
    }

    public int getWaterInterval() {
        return waterInterval;
    }

    public void setWaterInterval(int waterInterval) {
        this.waterInterval = waterInterval;
    }

    public int getRepotInterval() {
        return repotInterval;
    }

    public void setRepotInterval(int repotInterval) {
        this.repotInterval = repotInterval;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
