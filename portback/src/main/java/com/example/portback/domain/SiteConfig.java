package com.example.portback.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SiteConfig {

    @Id
    private String configKey;

    @Column(columnDefinition = "TEXT")
    private String configValue;
}
