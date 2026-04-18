package com.example.portback.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SkillGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String icon;
    private String color;

    @ElementCollection
    @CollectionTable(name = "skill_group_skills", joinColumns = @JoinColumn(name = "skill_group_id"))
    @Column(name = "skill")
    private List<String> skills;

    private int displayOrder;
}
