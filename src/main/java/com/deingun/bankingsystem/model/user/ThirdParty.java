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
@Table(name = "third_party")
public class ThirdParty extends User{

    @Column(name = "name", nullable = false, length = 64)
    @NotEmpty(message = "Name must be provided")
    private String name;

    @Column(name = "hashed_key", nullable = false)
    @NotEmpty(message = "Hashed key must be provided")
    private String hashedKey;

    public ThirdParty() {
    }

    public ThirdParty(String username, String password, LocalDate passwordDate, String name, String hashedKey) {
        super(username, password, passwordDate);
        this.name = name;
        this.hashedKey = hashedKey;
    }

    public ThirdParty(String username, String password, LocalDate passwordDate, Set<Role> roleSet, String name, String hashedKey) {
        super(username, password, passwordDate, roleSet);
        this.name = name;
        this.hashedKey = hashedKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }
}
