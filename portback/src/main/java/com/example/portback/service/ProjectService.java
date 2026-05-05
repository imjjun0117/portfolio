package com.example.portback.service;

import com.example.portback.domain.ProblemSolving;
import com.example.portback.domain.Project;
import com.example.portback.domain.ProjectScreen;
import com.example.portback.dto.ProjectRequest;
import com.example.portback.dto.ProjectScreenRequest;
import com.example.portback.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects(boolean all) {
        if (all) return projectRepository.findAll();
        return projectRepository.findAllByHiddenFalseOrderByIdDesc();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Project createProject(ProjectRequest request) {
        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .longDescription(request.getLongDescription())
                .period(request.getPeriod())
                .github(request.getGithub())
                .thumbnail(request.getThumbnail())
                .skills(request.getSkills())
                .roles(request.getRoles())
                .images(request.getImages())
                .vibeCoding(request.isVibeCoding())
                .hidden(request.isHidden())
                .problemSolving(ProblemSolving.builder()
                        .problem(request.getProblem())
                        .solution(request.getSolution())
                        .result(request.getResult())
                        .build())
                .build();

        applyScreens(project, request.getScreens());
        return projectRepository.save(project);
    }

    @Transactional
    public Optional<Project> updateProject(Long id, ProjectRequest request) {
        return projectRepository.findById(id).map(project -> {
            project.setTitle(request.getTitle());
            project.setDescription(request.getDescription());
            project.setLongDescription(request.getLongDescription());
            project.setPeriod(request.getPeriod());
            project.setGithub(request.getGithub());
            project.setThumbnail(request.getThumbnail());
            project.setSkills(request.getSkills());
            project.setRoles(request.getRoles());
            project.setImages(request.getImages());
            project.setVibeCoding(request.isVibeCoding());
            project.setHidden(request.isHidden());

            ProblemSolving ps = project.getProblemSolving();
            if (ps == null) {
                ps = new ProblemSolving();
                project.setProblemSolving(ps);
            }
            ps.setProblem(request.getProblem());
            ps.setSolution(request.getSolution());
            ps.setResult(request.getResult());

            project.getScreens().clear();
            applyScreens(project, request.getScreens());

            return projectRepository.save(project);
        });
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private void applyScreens(Project project, List<ProjectScreenRequest> screenRequests) {
        if (screenRequests == null) return;
        for (int i = 0; i < screenRequests.size(); i++) {
            ProjectScreenRequest sr = screenRequests.get(i);
            project.getScreens().add(ProjectScreen.builder()
                    .project(project)
                    .name(sr.getName())
                    .description(sr.getDescription())
                    .image(sr.getImage())
                    .displayOrder(i)
                    .features(sr.getFeatures() != null ? sr.getFeatures() : new ArrayList<>())
                    .roles(sr.getRoles() != null ? sr.getRoles() : new ArrayList<>())
                    .techs(sr.getTechs() != null ? sr.getTechs() : new ArrayList<>())
                    .highlights(sr.getHighlights() != null ? sr.getHighlights() : new ArrayList<>())
                    .build());
        }
    }
}
