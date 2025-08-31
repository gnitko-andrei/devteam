package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.dto.task.TaskCreationDto;
import by.teachmeskills.devteam.dto.task.TaskDto;
import by.teachmeskills.devteam.dto.task.UpdateTaskDto;
import by.teachmeskills.devteam.entity.task.Task;
import by.teachmeskills.devteam.entity.task.TaskStatus;
import by.teachmeskills.devteam.exception.ProjectNotFoundException;
import by.teachmeskills.devteam.exception.TaskNotFoundException;
import by.teachmeskills.devteam.mapper.TaskMapper;
import by.teachmeskills.devteam.repository.ProjectRepository;
import by.teachmeskills.devteam.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public List<TaskDto> findByProjectIdAndStatus(Long projectId, TaskStatus status) {
        return taskRepository.findByProjectIdAndStatus(projectId, status).stream().map(taskMapper::toTaskDto).toList();
    }

    public void addNewTask(TaskCreationDto taskCreationData) {
        var newTask = taskMapper.toEntity(taskCreationData);
        var projectId = taskCreationData.getProjectId();
        var project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        newTask.setProject(project);
        taskRepository.save(newTask);
    }

    public void updateTask(UpdateTaskDto updateTaskData) {
        var task = getTaskOrThrowException(updateTaskData.getId());
        Optional.ofNullable(updateTaskData.getStatus()).ifPresent(task::setStatus);
        Optional.ofNullable(updateTaskData.getSubmittedTime()).ifPresent(task::submitAdditionalTime);
        taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    private Task getTaskOrThrowException(long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }
}
