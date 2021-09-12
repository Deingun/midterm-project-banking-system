package com.deingun.bankingsystem.model.user;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Inheritance(strategy=InheritanceType.JOINED)
public class User implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "username",nullable = false, length = 64)
    @NotEmpty(message = "Username must be provided")
    @Size(max = 64)
    private String username;
    @Column(name = "password",nullable = false)
    @NotBlank
    private String password;
    @Column(name = "password_date",nullable = false)

    private LocalDate passwordDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name ="role_id"))
    private Set<Role> roleSet = new HashSet<>();


    public User() {
    }

    public User(String username, String password, LocalDate passwordDate) {
        this.username = username;
        this.password = password;
        this.passwordDate = passwordDate;
    }

    public User(String username, String password, LocalDate passwordDate, Set<Role> roleSet) {
        this.username = username;
        this.password = password;
        this.passwordDate = passwordDate;
        this.roleSet = roleSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getPasswordDate() {
        return passwordDate;
    }

    public void setPasswordDate(LocalDate passwordDate) {
        this.passwordDate = passwordDate;
    }

    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }
}

