package com.example.portback.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SkillGroupRequest {
    private String name;
    private String icon;
    private String color;
    private List<String> skills;
    private int displayOrder;
}
