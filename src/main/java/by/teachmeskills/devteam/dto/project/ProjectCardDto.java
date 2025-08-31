package by.teachmeskills.devteam.dto.project;

import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectCardDto {
    private Long id;
    private String name;
    private String specification;
    private ProjectStatus status;
    private String customerName;
    private String managerName;
    private Integer projectPrice;
}