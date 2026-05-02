package com.example.portback.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectScreenRequest {
    private String name;
    private String description;
    private String image;
    private int displayOrder;
    private List<String> features;
    private List<String> roles;
    private List<String> techs;
    private List<String> highlights;
}
