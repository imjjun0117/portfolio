package com.example.portback.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ProjectScreen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String image;

    private int displayOrder;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_screen_features", joinColumns = @JoinColumn(name = "screen_id"))
    @Column(name = "feature")
    @Builder.Default
    private List<String> features = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_screen_roles", joinColumns = @JoinColumn(name = "screen_id"))
    @Column(name = "role")
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_screen_techs", joinColumns = @JoinColumn(name = "screen_id"))
    @Column(name = "tech")
    @Builder.Default
    private List<String> techs = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_screen_highlights", joinColumns = @JoinColumn(name = "screen_id"))
    @Column(name = "highlight")
    @Builder.Default
    private List<String> highlights = new ArrayList<>();
}
