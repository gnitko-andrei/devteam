package by.teachmeskills.devteam.dto.project;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectCreationDto {
    private String newProjectName;
    private String newProjectSpecification;
    private Long managerId;
    private Long customerId;
}