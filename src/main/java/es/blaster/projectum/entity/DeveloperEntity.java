package es.blaster.projectum.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

@Entity
@Table(name = "developer")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class DeveloperEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    @Column(name = "last_name")
    private String lastname;
    private String email;
    private String username;

    @OneToMany(mappedBy = "developer", fetch = FetchType.LAZY)
    private final List<IssueEntity> issues;

    public DeveloperEntity() {

        this.issues = new ArrayList<>();
    }

    public DeveloperEntity(Long id) {

        this.issues = new ArrayList<>();
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIssues() {
        return issues.size();
    }

    @PreRemove
    public void nullify() {
        this.issues.forEach(c -> c.setDeveloper(null));
    }
}
