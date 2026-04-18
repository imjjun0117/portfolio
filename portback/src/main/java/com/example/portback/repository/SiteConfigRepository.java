package com.example.portback.repository;

import com.example.portback.domain.SiteConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteConfigRepository extends JpaRepository<SiteConfig, String> {
}
