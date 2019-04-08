package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.Task;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.repository.ProjectRepository;
import by.teachmeskills.devteam.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TaskService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TaskRepository taskRepository;


    public void addNewTask(Long projectId, Map<String, String> formData) {
        String name = formData.get("name");
        String description = formData.get("description");
        Project project = projectRepository.findById(projectId).get();

        Task task = new Task(name, description, project);
        taskRepository.save(task);
    }

    public Iterable<Task> findAll() {
        return taskRepository.findAll();
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
        Task task = taskRepository.findById(Long.parseLong(formData.get("id"))).get();
        Integer formTime = Integer.parseInt(formData.get("time"));
        Integer time = task.getTime() + formTime;
        task.setTime(time);
        Integer price = task.getPrice() + formTime * user.getPrice();
        task.setPrice(price);
        taskRepository.save(task);
    }

    private void updateStatus(Map<String, String> formData) {
        Task task = taskRepository.findById(Long.parseLong(formData.get("id"))).get();
        task.setStatus(formData.get("status"));
        taskRepository.save(task);
    }
}
