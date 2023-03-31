package com.iscreammm.restapi.model;

import jakarta.persistence.*;

import java.util.Arrays;

@Entity(name = "Profile")
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, name = "name")
    private String name;

    @Column(nullable = false, name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne(targetEntity = Gender.class)
    @JoinColumn(name = "gender_id")
    private Gender gender;

    public Profile() {
    }

    public Profile(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public User getUser() {
        return this.user;
    }

    public Gender getGender() {
        return this.gender;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id + ", " +
                "name: '" + name + "', " +
                "photo: " + Arrays.toString(photo) + ", " +
                "gender: '" + gender.getGender() +
                "'}";
    }
}
