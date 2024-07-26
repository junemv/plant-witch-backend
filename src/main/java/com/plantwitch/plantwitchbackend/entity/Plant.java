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
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    private User user;

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
}
