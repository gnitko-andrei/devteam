package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/home")
    public String home(Model model) {

        return "home";
    }

    @GetMapping("/main")
    public String main(Model model) {
        Iterable<Project> projects = projectRepository.findAll();

        model.addAttribute("projects", projects);

        return "main";
    }

    @PostMapping("/main")
    public String add(@RequestParam String name, @RequestParam String specification,
                      @RequestParam String status, Model model) {

        Project project = new Project(name, specification, status);
        projectRepository.save(project);

        Iterable<Project> projects = projectRepository.findAll();
        model.addAttribute("projects", projects);

        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Model model) {
        Iterable<Project> projects;

        if (filter != null && !filter.isEmpty()) {
            projects = projectRepository.findByName(filter);
        } else {
            projects = projectRepository.findAll();
        }

        model.addAttribute("projects", projects);
        return "main";
    }

}
