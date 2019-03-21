package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/")
    public String home(Model model) {

        return "home";
    }

    @GetMapping("/projects")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Project> projects = projectRepository.findAll();

        if (filter != null && !filter.isEmpty()) {
            projects = projectRepository.findByName(filter);
        } else {
            projects = projectRepository.findAll();
        }

        model.addAttribute("projects", projects);
        model.addAttribute("filter", filter);

        return "projects";
    }

    @PostMapping("/projects")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String specification,
            @RequestParam String status,
            Model model) {

        Project project = new Project(name, specification, status, user);
        projectRepository.save(project);

        Iterable<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);

        return "projects";
    }
}
