package com.example.portback.controller;

import com.example.portback.domain.Experience;
import com.example.portback.dto.ExperienceRequest;
import com.example.portback.repository.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceRepository experienceRepository;

    @GetMapping
    public List<Experience> getAll() {
        return experienceRepository.findAllByOrderByDisplayOrderAsc();
    }

    @PostMapping
    public ResponseEntity<Experience> create(@RequestBody ExperienceRequest req) {
        Experience exp = Experience.builder()
                .company(req.getCompany())
                .period(req.getPeriod())
                .duration(req.getDuration())
                .role(req.getRole())
                .type(req.getType())
                .description(req.getDescription())
                .tags(req.getTags())
                .current(req.isCurrent())
                .displayOrder(req.getDisplayOrder())
                .build();
        return ResponseEntity.ok(experienceRepository.save(exp));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Experience> update(@PathVariable Long id, @RequestBody ExperienceRequest req) {
        return experienceRepository.findById(id).map(exp -> {
            exp.setCompany(req.getCompany());
            exp.setPeriod(req.getPeriod());
            exp.setDuration(req.getDuration());
            exp.setRole(req.getRole());
            exp.setType(req.getType());
            exp.setDescription(req.getDescription());
            exp.setTags(req.getTags());
            exp.setCurrent(req.isCurrent());
            exp.setDisplayOrder(req.getDisplayOrder());
            return ResponseEntity.ok(experienceRepository.save(exp));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
