package com.de.hiking.models;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Models the <code>Trail</code> entity and its properties
 */
@ApiModel("Trail")
@Entity
@Table(name = "trail", schema = "hiking")
public class Trail {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, name = "trail_id")
    private UUID trailId;

    @Column
    private String name;

    @Column
    private double price;

    @Column(name = "min_age")
    private int minAge;

    @Column(name = "max_age")
    private int maxAge;

    @Column(name = "start_time")
    private LocalTime startTrail;

    @Column(name = "end_time")
    private LocalTime endTrail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public LocalTime getStartTrail() {
        return startTrail;
    }

    public void setStartTrail(LocalTime startTrail) {
        this.startTrail = startTrail;
    }

    public LocalTime getEndTrail() {
        return endTrail;
    }

    public void setEndTrail(LocalTime endTrail) {
        this.endTrail = endTrail;
    }

    public UUID getTrailId() {
        return trailId;
    }

    public void setTrailId(UUID trailId) {
        this.trailId = trailId;
    }


}
