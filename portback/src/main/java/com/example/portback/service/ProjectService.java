package com.example.portback.service;

import com.example.portback.domain.ProblemSolving;
import com.example.portback.domain.Project;
import com.example.portback.dto.ProjectRequest;
import com.example.portback.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
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
                .problemSolving(ProblemSolving.builder()
                        .problem(request.getProblem())
                        .solution(request.getSolution())
                        .result(request.getResult())
                        .build())
                .build();
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

            ProblemSolving ps = project.getProblemSolving();
            if (ps == null) {
                ps = new ProblemSolving();
                project.setProblemSolving(ps);
            }
            ps.setProblem(request.getProblem());
            ps.setSolution(request.getSolution());
            ps.setResult(request.getResult());

            return projectRepository.save(project);
        });
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}
