package com.deingun.bankingsystem.security;

import com.deingun.bankingsystem.model.user.Role;
import com.deingun.bankingsystem.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : user.getRoleSet()){
            authorities.add(new SimpleGrantedAuthority("ROLE_" +role.getRole().toString()));

        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
//        LocalDate expiryDate = LocalDate.now().minusYears(1L);
//        if(user.getPasswordDate().compareTo(expiryDate)<0){
//            return false;
//        }
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
