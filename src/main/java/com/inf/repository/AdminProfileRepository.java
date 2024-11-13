package com.inf.repository;

import com.inf.model.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProfileRepository extends JpaRepository<AdminProfile, Long> {
}
