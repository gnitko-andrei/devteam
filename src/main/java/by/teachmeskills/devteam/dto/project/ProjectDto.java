package by.teachmeskills.devteam.dto.project;

import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectDto {
    private Long id;
    private String name;
    private String specification;
    private ProjectStatus status;
    private UserDto customer;
    private UserDto manager;
    private List<UserDto> developers;
    private Integer projectPrice;
}