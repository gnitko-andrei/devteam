package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.dto.project.ProjectCreationDto;
import by.teachmeskills.devteam.dto.project.ProjectFiltersDto;
import by.teachmeskills.devteam.dto.project.UpdateProjectDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.project.ProjectStatus;
import by.teachmeskills.devteam.service.ProjectService;
import by.teachmeskills.devteam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static by.teachmeskills.devteam.util.TextUtils.replaceBrOnHyphenation;

@Controller
@RequestMapping("/projects")
@PreAuthorize("hasAnyAuthority('CUSTOMER', 'DEVELOPER', 'MANAGER')")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping
    public String getUserProjects(@AuthenticationPrincipal(expression = "id") Long userId,
                                  @AuthenticationPrincipal(expression = "roles") Set<Role> roles,
                                  @ModelAttribute ProjectFiltersDto filters,
                                  Model model) {
        filters.setUserId(userId);
        filters.setUserRoles(roles);
        var projects = projectService.findUserProjects(filters);
        var managers = userService.getAllManagers();

        model.addAttribute("projectStatuses", ProjectStatus.visibleForFiltering());
        model.addAttribute("userProjectsList", projects);
        model.addAttribute("nameFilter", filters.getNameFilter());
        model.addAttribute("statusFilter", filters.getStatusFilter());
        model.addAttribute("managers", managers);

        return "projects";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public String addNewProject(
            @AuthenticationPrincipal(expression = "id") Long customerId,
            @ModelAttribute ProjectCreationDto newProjectData) {
        newProjectData.setCustomerId(customerId);
        projectService.addNewProject(newProjectData);

        return "redirect:/projects";
    }

    @GetMapping("{projectId}")
    public String getProjectInfo(@AuthenticationPrincipal(expression = "id") Long userId, @PathVariable Long projectId, Model model) {
        var project = projectService.findById(projectId);

        model.addAttribute("project", project);

        if (projectService.isProjectVisibleForUser(projectId, userId)) {
            return "projectInfo";
        }
        return "/error";
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public String deleteProject(Long projectId) {
        projectService.deleteById(projectId);

        return "redirect:/projects";
    }

    @GetMapping("/edit/{projectId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public String getProjectEditor(@AuthenticationPrincipal(expression = "id") Long userId, @PathVariable Long projectId, Model model) {
        var project = projectService.findById(projectId);
        var specification = replaceBrOnHyphenation(project.getSpecification());
        var managers = userService.getAllManagers();
        var developers = userService.getAllDevelopers();

        model.addAttribute("project", project);
        model.addAttribute("specification", specification);
        model.addAttribute("statuses", ProjectStatus.visibleForFiltering());
        model.addAttribute("managers", managers);
        model.addAttribute("developers", developers);

        if (projectService.isProjectVisibleForUser(projectId, userId)) {
            return "projectEditor";
        }
        return "/error";
    }

    @PostMapping("/edit/{projectId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public String updateProject(@AuthenticationPrincipal(expression = "roles") Set<Role> roles,
                                @PathVariable Long projectId,
                                @ModelAttribute UpdateProjectDto updatedProjectData) {
        updatedProjectData.setId(projectId);
        projectService.updateProject(roles, updatedProjectData);

        return "redirect:/projects/{projectId}";
    }
}
