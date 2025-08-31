package by.teachmeskills.devteam.mapper;

import by.teachmeskills.devteam.dto.project.ProjectCardDto;
import by.teachmeskills.devteam.dto.project.ProjectDto;
import by.teachmeskills.devteam.entity.project.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProjectMapper {

    private final UserMapper userMapper;

    public ProjectDto toProjectDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .specification(project.getSpecification())
                .status(project.getStatus())
                .customer(userMapper.toUserDto(project.getCustomer()))
                .manager(userMapper.toUserDto(project.getManager()))
                .developers(userMapper.toUserDtoSet(project.getDevelopers()))
                .projectPrice(project.getProjectPrice())
                .build();
    }

    public ProjectCardDto toProjectCardDto(Project project) {
        return ProjectCardDto.builder()
                .id(project.getId())
                .name(project.getName())
                .specification(project.getSpecification())
                .status(project.getStatus())
                .customerName(project.getCustomerName())
                .managerName(project.getManagerName())
                .projectPrice(project.getProjectPrice())
                .build();
    }
}