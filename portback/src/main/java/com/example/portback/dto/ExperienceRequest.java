package com.example.portback.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ExperienceRequest {
    private String company;
    private String period;
    private String duration;
    private String role;
    private String type;
    private String description;
    private List<String> tags;
    private boolean current;
    private int displayOrder;
    private boolean hidden;
}
