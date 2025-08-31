package by.teachmeskills.devteam.dto.project;

import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateProjectDto {
    private Long id;
    private String name;
    private String specification;
    private ProjectStatus status;
    private Long managerId;
    private List<Long> developerIds;
}