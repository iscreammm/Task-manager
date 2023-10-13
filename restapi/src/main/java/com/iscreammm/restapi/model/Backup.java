package com.iscreammm.restapi.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "Backup")
@Table(name = "backup")
public class Backup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "backup", columnDefinition = "text")
    private String backup;

    @Column(nullable = false, name = "date")
    private Date date;

    @OneToOne(mappedBy = "backup", targetEntity = User.class)
    private User user;

    public Backup() {
        this.backup = null;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{" +
                "backup: '" + backup + "', " +
                "date: '" + date + "'" +
                "}";
    }
}
