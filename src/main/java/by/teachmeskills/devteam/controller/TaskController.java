package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.dto.task.TaskCreationDto;
import by.teachmeskills.devteam.dto.task.UpdateTaskDto;
import by.teachmeskills.devteam.entity.attributes.task.TaskStatus;
import by.teachmeskills.devteam.service.ProjectService;
import by.teachmeskills.devteam.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects/{projectId}/tasks")
@PreAuthorize("hasAnyAuthority('CUSTOMER', 'DEVELOPER', 'MANAGER')")
@RequiredArgsConstructor
public class TaskController {

    public static final String REDIRECT_PROJECTS_PROJECT_ID_TASKS = "redirect:/projects/{projectId}/tasks";
    private final TaskService taskService;
    private final ProjectService projectService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('DEVELOPER', 'MANAGER')")
    public String getAllProjectTasks(@PathVariable Long projectId,
            Model model,
            TaskStatus statusFilter) {
        var tasks = taskService.findByProjectIdAndStatus(projectId, statusFilter);
        var project = projectService.findById(projectId);

        model.addAttribute("taskStatuses", TaskStatus.values());
        model.addAttribute("project", project);
        model.addAttribute("tasks", tasks);
        model.addAttribute("statusFilter", statusFilter);

        return "tasksList";
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('DEVELOPER', 'MANAGER')")
    public String createNewTask(@ModelAttribute TaskCreationDto taskCreationData) {
        taskService.addNewTask(taskCreationData);

        return REDIRECT_PROJECTS_PROJECT_ID_TASKS;
    }

    @PostMapping("{taskId}")
    @PreAuthorize("hasAnyAuthority('DEVELOPER')")
    public String updateTask(@PathVariable Long taskId,
                             @ModelAttribute UpdateTaskDto updateTaskData) {
        updateTaskData.setId(taskId);
        taskService.updateTask(updateTaskData);
        return REDIRECT_PROJECTS_PROJECT_ID_TASKS;
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('MANAGER')")
    public String deleteTask(@RequestParam Long id) {
        taskService.deleteById(id);
        return REDIRECT_PROJECTS_PROJECT_ID_TASKS;
    }
}
