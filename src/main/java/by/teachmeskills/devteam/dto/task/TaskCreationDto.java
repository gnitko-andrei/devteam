package by.teachmeskills.devteam.dto.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskCreationDto {
    private Long projectId;
    private String name;
    private String description;
}