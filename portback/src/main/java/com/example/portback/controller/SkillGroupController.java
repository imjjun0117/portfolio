package com.example.portback.controller;

import com.example.portback.domain.SkillGroup;
import com.example.portback.dto.SkillGroupRequest;
import com.example.portback.repository.SkillGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillGroupController {

    private final SkillGroupRepository skillGroupRepository;

    @GetMapping
    public List<SkillGroup> getAll() {
        return skillGroupRepository.findAllByOrderByDisplayOrderAsc();
    }

    @PostMapping
    public ResponseEntity<SkillGroup> create(@RequestBody SkillGroupRequest req) {
        SkillGroup group = SkillGroup.builder()
                .name(req.getName())
                .icon(req.getIcon())
                .color(req.getColor())
                .skills(req.getSkills())
                .displayOrder(req.getDisplayOrder())
                .build();
        return ResponseEntity.ok(skillGroupRepository.save(group));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillGroup> update(@PathVariable Long id, @RequestBody SkillGroupRequest req) {
        return skillGroupRepository.findById(id).map(group -> {
            group.setName(req.getName());
            group.setIcon(req.getIcon());
            group.setColor(req.getColor());
            group.setSkills(req.getSkills());
            group.setDisplayOrder(req.getDisplayOrder());
            return ResponseEntity.ok(skillGroupRepository.save(group));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        skillGroupRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
