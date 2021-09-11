package com.deingun.bankingsystem.model.user;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "admin")
public class Admin extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    @Column(name = "name", nullable = false, length = 64)
    @NotEmpty(message = "Name must be provided")
    private String name;

    public Admin() {
    }

    public Admin(String username, String password, LocalDate passwordDate, String name) {
        super(username, password, passwordDate);
        this.name = name;
    }

    public Admin(String username, String password, LocalDate passwordDate, Set<Role> roleSet, String name) {
        super(username, password, passwordDate, roleSet);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
