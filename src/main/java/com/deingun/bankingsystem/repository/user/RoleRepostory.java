package com.deingun.bankingsystem.repository.user;

import com.deingun.bankingsystem.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepostory extends JpaRepository<Role, Long> {
}
