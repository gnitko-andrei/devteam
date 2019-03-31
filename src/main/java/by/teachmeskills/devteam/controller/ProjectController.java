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

import java.util.Map;

@Controller
@RequestMapping("/projects")
@PreAuthorize("hasAnyAuthority('CUSTOMER', 'DEVELOPER', 'MANAGER')")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Project> projects = projectService.findAll();

        if (filter != null && !filter.isEmpty()) {
            projects = projectService.findByName(filter);
        } else {
            projects = projectService.findAll();
        }

        model.addAttribute("projects", projects);
        model.addAttribute("filter", filter);

        return "projects";
    }

    @PostMapping
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String specification,
            Model model) {

        projectService.addNewProject(name, specification, user);

        Iterable<Project> projects = projectService.findAll();
        model.addAttribute("projects", projects);

        return "projects";
    }

    @GetMapping("{projectId}")
    public String projectInfo(@PathVariable Long projectId, Model model) {


        Project project = projectService.findById(projectId);
        model.addAttribute("project", project);

        return "projectInfo";
    }

    @DeleteMapping
    public String delete(@RequestParam Long id) {
        projectService.deleteById(id);
        return "redirect:/projects";
    }

    @GetMapping("/edit/{projectId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public String editProject(@PathVariable Long projectId, Model model) {


        Project project = projectService.findById(projectId);
        model.addAttribute("project", project);
        String specification = projectService.replaceBrOnHyphenation(project.getSpecification());
        model.addAttribute("specification", specification);

        return "projectEditor";
    }

    @PostMapping("/edit/{projectId}")
    public String updateProject(@PathVariable Long projectId,
                                @RequestParam Map<String, String> formData,
                                Model model) {
        System.out.println(formData);

        projectService.updateProject(projectId, formData);

        return "redirect:/projects/{projectId}";
    }
}
