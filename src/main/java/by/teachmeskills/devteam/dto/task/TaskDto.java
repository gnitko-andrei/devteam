package by.teachmeskills.devteam.dto.task;

import by.teachmeskills.devteam.entity.attributes.task.TaskStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private Integer time;
    private Integer price;
    private TaskStatus status;
}