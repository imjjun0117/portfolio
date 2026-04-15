package com.example.portback.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectRequest {
    private String title;
    private String description;
    private String longDescription;
    private String period;
    private String github;
    private String thumbnail;
    private List<String> skills;
    private List<String> roles;
    private List<String> images;
    private String problem;
    private String solution;
    private String result;
}
