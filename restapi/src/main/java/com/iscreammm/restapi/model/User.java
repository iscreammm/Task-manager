package com.iscreammm.restapi.model;

import jakarta.persistence.*;

@Entity(name = "User")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 16, name = "login")
    private String login;

    @Column(nullable = false, length = 16, name = "password")
    private String password;

    @OneToOne(mappedBy = "user", targetEntity = Profile.class)
    private Profile profile;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public String getPassword() {
        return password;
    }
}
