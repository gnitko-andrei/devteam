package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.Task;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.repository.ProjectRepository;
import by.teachmeskills.devteam.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private static final String PROJECT_NOT_FOUND_ERROR_MSG = "Project with id %s not found";
    private static final String TASK_NOT_FOUND_ERROR_MSG = "Task with id %s not found";

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public void addNewTask(Long projectId, Map<String, String> formData) {
        String name = formData.get("name");
        String description = formData.get("description");
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalStateException(String.format(PROJECT_NOT_FOUND_ERROR_MSG, projectId)));

        Task task = new Task(name, description, project);
        taskRepository.save(task);
    }

    public List<Task> findAll(Long projectId) {
        Iterable<Task> allTasks = taskRepository.findAll();
        List<Task> currentProjectTasks = new ArrayList<>();
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalStateException(String.format(PROJECT_NOT_FOUND_ERROR_MSG, projectId)));
        for (Task task : allTasks) {
            if (task.getProject().equals(project)) {
                currentProjectTasks.add(task);
            }
        }
        return currentProjectTasks;
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public void postTask(User user, Long projectId, Map<String, String> formData) {
        if (user.getRoles().contains(Role.MANAGER)) {
            addNewTask(projectId, formData);
        }
        if (user.getRoles().contains(Role.DEVELOPER)) {
            if (formData.containsKey("status")) {
                updateStatus(formData);
            }
            if (formData.containsKey("time")) {
                updateTime(user, formData);
            }

        }
    }

    private void updateTime(User user, Map<String, String> formData) {

        long taskId = Long.parseLong(formData.get("id"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException(String.format(TASK_NOT_FOUND_ERROR_MSG, taskId)));
        Integer formTime = Integer.parseInt(formData.get("time"));
        Integer time = task.getTime() + formTime;
        task.setTime(time);
        Integer price = task.getPrice() + formTime * user.getPrice();
        task.setPrice(price);
        taskRepository.save(task);
    }

    private void updateStatus(Map<String, String> formData) {
        long taskId = Long.parseLong(formData.get("id"));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException(String.format(TASK_NOT_FOUND_ERROR_MSG, taskId)));
        task.setStatus(formData.get("status"));
        taskRepository.save(task);
    }

    public List<Task> findByStatus(String status) {
        return taskRepository.findByStatus(status);
    }
}
