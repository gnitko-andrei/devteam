package by.teachmeskills.devteam.dto.task;

import by.teachmeskills.devteam.entity.task.TaskStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTaskDto {
    private Long id;
    private Integer submittedTime;
    private TaskStatus status;
}