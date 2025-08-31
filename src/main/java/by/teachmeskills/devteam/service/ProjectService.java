package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.dto.project.*;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import by.teachmeskills.devteam.exception.ProjectNameAlreadyInUseException;
import by.teachmeskills.devteam.exception.ProjectNotFoundException;
import by.teachmeskills.devteam.exception.UserNotFoundException;
import by.teachmeskills.devteam.mapper.ProjectMapper;
import by.teachmeskills.devteam.repository.ProjectRepository;
import by.teachmeskills.devteam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static by.teachmeskills.devteam.util.TextUtils.replaceHyphenationOnBr;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;

    public ProjectDto findById(Long id) {
        return projectRepository.findById(id)
                .map(projectMapper::toProjectDto)
                .orElseThrow(() -> new ProjectNotFoundException(id));
    }

    public List<ProjectCardDto> findUserProjects(ProjectFiltersDto searchCriteria) {
        var roles = searchCriteria.getUserRoles();
        List<Project> projects;
        if (roles.contains(Role.CUSTOMER)) {
            projects = projectRepository.findCustomerProjects(searchCriteria);
        } else if (roles.contains(Role.MANAGER)) {
            projects = projectRepository.findManagerProjects(searchCriteria);
        } else if (roles.contains(Role.DEVELOPER)) {
            projects = projectRepository.findDeveloperProjects(searchCriteria);
        } else {
            projects = List.of();
        }
        return projects.stream()
                .map(projectMapper::toProjectCardDto)
                .toList();
    }

    public boolean isProjectVisibleForUser(Long projectId, Long userId) {
        return projectRepository.findById(projectId).map(project -> project.isVisibleForUser(userId))
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    public void addNewProject(ProjectCreationDto newProjectData) {
        var newProjectName = newProjectData.getNewProjectName();
        projectRepository.findByName(newProjectName).ifPresent(project -> {
            throw new ProjectNameAlreadyInUseException(newProjectName);
        });
        var customerId = newProjectData.getCustomerId();
        var managerId = newProjectData.getManagerId();
        var specification = replaceHyphenationOnBr(newProjectData.getNewProjectSpecification());
        var manager = userRepository.findById(managerId)
                .orElseThrow(() -> new UserNotFoundException(managerId));
        var customer = userRepository.findById(customerId)
                .orElseThrow(() -> new UserNotFoundException(customerId));
        var project = new Project(newProjectName, specification, ProjectStatus.NEW, customer, manager);
        save(project);
    }

    public void updateProject(Set<Role> editorRoles, UpdateProjectDto updatedProjectData) {
        final var projectId = updatedProjectData.getId();
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        if (editorRoles.contains(Role.CUSTOMER)) {
            project.setName(updatedProjectData.getName());
            project.setSpecification(replaceHyphenationOnBr(updatedProjectData.getSpecification()));
            project.setManager(findUserByIdOrThrowException(updatedProjectData.getManagerId()));
        }
        if (editorRoles.contains(Role.MANAGER)) {
            project.setStatus(updatedProjectData.getStatus());
            project.setDevelopers(new ArrayList<>(updatedProjectData.getDeveloperIds() == null ? List.of() : updatedProjectData.getDeveloperIds().stream().map(this::findUserByIdOrThrowException).toList()));
        }
        projectRepository.save(project);
    }

    public void deleteById(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    private User findUserByIdOrThrowException(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void save(Project project) {
        projectRepository.save(project);
    }

}
