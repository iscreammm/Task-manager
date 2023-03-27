package com.iscreammm.restapi.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Gender")
@Table(name = "gender")
public class Gender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "gender")
    private String gender;

    @OneToMany(mappedBy = "gender", targetEntity = Profile.class)
    private Set<Profile> profiles;

    public Gender() {
    }

    public Gender(String gender) {
        this.gender = gender;
        this.profiles = new HashSet<>();
    }

    public void addProfile(Profile profile) {
        this.profiles.add(profile);
    }

    public String getGender() {
        return this.gender;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    public Set<Profile> getProfiles() {
        return this.profiles;
    }
}
