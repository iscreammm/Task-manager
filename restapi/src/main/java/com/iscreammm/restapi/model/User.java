package com.iscreammm.restapi.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

@Entity(name = "User")
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 30, name = "username")
    private String username;

    @Column(nullable = false, length = 60, name = "password")
    private String password;

    @Column(unique = true, nullable = false, length = 40, name = "mail")
    private String mail;

    @Column(length = 32, name = "code")
    private String code;

    @Column(nullable = false, name = "isActive")
    private boolean isActive;

    @OneToOne(targetEntity = Backup.class)
    @JoinColumn(name = "backup_id")
    private Backup backup;

    @OneToOne(mappedBy = "user", targetEntity = Profile.class)
    private Profile profile;

    public User() {
    }

    public User(String username, String password, String mail, String code) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.code = code;
        this.isActive = false;
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

    @Override
    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Backup getBackup() {
        return backup;
    }

    public void setBackup(Backup backup) {
        this.backup = backup;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }
}
