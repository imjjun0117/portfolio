package com.example.portback.controller;

import com.example.portback.domain.SiteConfig;
import com.example.portback.repository.SiteConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/config")
@RequiredArgsConstructor
public class SiteConfigController {

    private final SiteConfigRepository siteConfigRepository;

    @GetMapping
    public Map<String, String> getAll() {
        return siteConfigRepository.findAll().stream()
                .collect(Collectors.toMap(SiteConfig::getConfigKey, SiteConfig::getConfigValue));
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> updateAll(@RequestBody Map<String, String> updates) {
        updates.forEach((key, value) ->
                siteConfigRepository.save(new SiteConfig(key, value))
        );
        return ResponseEntity.ok(updates);
    }
}
