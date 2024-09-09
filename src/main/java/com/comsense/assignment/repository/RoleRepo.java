package com.comsense.assignment.repository;

import com.comsense.assignment.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<UserRole, Long> {
}