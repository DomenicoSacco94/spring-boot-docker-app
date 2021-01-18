package com.de.hiking.models;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.util.UUID;

/**
 * Models the <code>Hiking</code> entity and its properties
 */
@ApiModel("Hiker")
@Entity
@Table(name = "hiker", schema = "hiking")
public class Hiker {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, name = "hiker_id")
    private UUID hikerId;

    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String mail;
    @Column
    private int age;

    public UUID getHikerId() {
        return hikerId;
    }

    public void setHikerId(UUID hikerId) {
        this.hikerId = hikerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
