package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.Task;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.service.ProjectService;
import by.teachmeskills.devteam.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projects/{projectId}/tasks")
@PreAuthorize("hasAnyAuthority('CUSTOMER', 'DEVELOPER', 'MANAGER')")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectService projectService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('DEVELOPER', 'MANAGER')")
    public String tasks(@AuthenticationPrincipal User user, @PathVariable Long projectId, Model model, @RequestParam(required = false, defaultValue = "") String filter) {

        Project project = projectService.findById(projectId);
        List<Task> tasks;

        if (filter != null && !filter.isEmpty()) {
            tasks = taskService.findByStatus(filter);
        } else {
            tasks = taskService.findAll(projectId);
        }

        model.addAttribute("project", project);
        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);

        if (project.getManager().equals(user) || project.getDevelopers().contains(user)) {
            return "tasksList";
        }
        return "/error";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('DEVELOPER', 'MANAGER')")
    public String addTask(@AuthenticationPrincipal User user, @PathVariable Long projectId, @RequestParam Map<String, String> formData, Model model) {

        taskService.postTask(user, projectId, formData);

        return "redirect:/projects/{projectId}/tasks";
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public String deleteTask(@RequestParam Long id) {
        taskService.deleteById(id);
        return "redirect:/projects/{projectId}/tasks";
    }
}
