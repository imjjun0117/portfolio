package com.example.portback.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String period;
    private String duration;
    private String role;
    private String type;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "experience_tags", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "tag")
    private List<String> tags;

    private boolean current;
    private int displayOrder;

    @Column(columnDefinition = "boolean not null default false")
    private boolean hidden;
}
