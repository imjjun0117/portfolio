package com.example.portback.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "project_skills", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "skill")
    private List<String> skills;

    @ElementCollection
    @CollectionTable(name = "project_roles", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "role")
    private List<String> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_solving_id")
    private ProblemSolving problemSolving;

    private String github;
    private String thumbnail;
    private String period;

    @Column(columnDefinition = "TEXT")
    private String longDescription;

    @ElementCollection
    @CollectionTable(name = "project_images", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Column(columnDefinition = "boolean not null default false")
    private boolean vibeCoding;

    @Column(columnDefinition = "boolean not null default false")
    private boolean hidden;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<ProjectScreen> screens = new ArrayList<>();
}
