package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static by.teachmeskills.devteam.util.TextUtils.replaceBrOnHyphenation;

@Controller
@RequestMapping("/projects")
@PreAuthorize("hasAnyAuthority('CUSTOMER', 'DEVELOPER', 'MANAGER')")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public String main(@AuthenticationPrincipal User user,
                       @RequestParam(required = false, defaultValue = "") String nameFilter,
                       @RequestParam(required = false, defaultValue = "") String statusFilter,
                       Model model) {
        Iterable<Project> projects = projectService.findAll();

        List<Project> userProjects = new ArrayList<>();

        if (nameFilter != null && !nameFilter.isEmpty()) {
            projects = projectService.findByName(nameFilter);
        } else if (statusFilter != null && !statusFilter.isEmpty()) {
            projects = projectService.findByStatus(statusFilter);
        } else {
            projects = projectService.findAll();
        }

        for (Project project : projects) {
            if (project.getCustomer().equals(user) || project.getManager().equals(user) || project.getDevelopers().contains(user)) {

                userProjects.add(project);
            }
        }

        List<User> managers = projectService.getAllManagers();

        model.addAttribute("projects", userProjects);
        model.addAttribute("nameFilter", nameFilter);
        model.addAttribute("managers", managers);

        return "projects";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String specification,
            @RequestParam Long managerId,
            Model model) {

        projectService.addNewProject(name, specification, user, managerId);

        Iterable<Project> projects = projectService.findAll();
        model.addAttribute("projects", projects);

        return "projects";
    }

    @GetMapping("{projectId}")
    public String projectInfo(@AuthenticationPrincipal User user, @PathVariable Long projectId, Model model) {

        Project project = projectService.findById(projectId);

        model.addAttribute("project", project);

        if (project.getCustomer().equals(user) || project.getManager().equals(user) || project.getDevelopers().contains(user)) {
            return "projectInfo";
        }
        return "/error";
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public String delete(@RequestParam Long id) {
        projectService.deleteById(id);
        return "redirect:/projects";
    }

    @GetMapping("/edit/{projectId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public String editProject(@AuthenticationPrincipal User user, @PathVariable Long projectId, Model model) {


        Project project = projectService.findById(projectId);

        String specification = replaceBrOnHyphenation(project.getSpecification());

        List<User> managers = projectService.getAllManagers();
        List<User> developers = projectService.getAllDevelopers();

        model.addAttribute("project", project);
        model.addAttribute("specification", specification);
        model.addAttribute("managers", managers);
        model.addAttribute("developers", developers);

        if (project.getCustomer().equals(user) || project.getManager().equals(user) || project.getDevelopers().contains(user)) {
            return "projectEditor";
        }
        return "/error";
    }

    @PostMapping("/edit/{projectId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public String updateProject(@AuthenticationPrincipal User user,
                                @PathVariable Long projectId,
                                @RequestParam Map<String, String> formData,
                                Model model) {
        System.out.println(formData);

        projectService.updateProject(user, projectId, formData);

        return "redirect:/projects/{projectId}";
    }
}
