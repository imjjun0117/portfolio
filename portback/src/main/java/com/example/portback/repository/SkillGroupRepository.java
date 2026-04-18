package com.example.portback.repository;

import com.example.portback.domain.SkillGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillGroupRepository extends JpaRepository<SkillGroup, Long> {
    List<SkillGroup> findAllByOrderByDisplayOrderAsc();
}
