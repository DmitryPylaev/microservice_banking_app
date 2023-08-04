package com.neoflex.java2023.repository;

import com.neoflex.java2023.model.jsonb.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {
}
