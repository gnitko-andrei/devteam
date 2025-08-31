package by.teachmeskills.devteam.mapper;

import by.teachmeskills.devteam.dto.task.TaskCreationDto;
import by.teachmeskills.devteam.dto.task.TaskDto;
import by.teachmeskills.devteam.entity.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TaskMapper {

    public TaskDto toTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .time(task.getTime())
                .price(task.getPrice())
                .status(task.getStatus())
                .build();
    }

    public Task toEntity(TaskCreationDto dto) {
        return new Task(dto.getName(), dto.getDescription());
    }
}