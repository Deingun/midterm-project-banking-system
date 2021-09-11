package com.deingun.bankingsystem.model.user;

import com.deingun.bankingsystem.utils.Address;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "account_holder")
public class AccountHolder extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    @NotEmpty(message = "Name must be provided")
    private String name;
    @Column(name = "nif", nullable = false, length = 10)
    @NotEmpty(message = "NIF must be provided")
    private String nif;
    @Column(name = "date_of_birth", nullable = false)

    private LocalDate dateOfBirth;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "street")),
            @AttributeOverride(name = "city", column = @Column(name = "city")),
            @AttributeOverride(name = "country", column = @Column(name = "country")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "postal_code"))
    })
    private Address address;
    @Column(name = "mailing_address")
    private String mailingAddress;

    public AccountHolder() {
    }

    public AccountHolder(String username, String password, LocalDate passwordDate, String name, String nif, LocalDate dateOfBirth, Address address, String mailingAddress) {
        super(username, password, passwordDate);
        this.name = name;
        this.nif = nif;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.mailingAddress = mailingAddress;
    }

    public AccountHolder(String username, String password, LocalDate passwordDate, Set<Role> roleSet, String name, String nif, LocalDate dateOfBirth, Address address, String mailingAddress) {
        super(username, password, passwordDate, roleSet);
        this.name = name;
        this.nif = nif;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.mailingAddress = mailingAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
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
