package com.deingun.bankingsystem.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "admin")
public class Admin extends User{

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
}
