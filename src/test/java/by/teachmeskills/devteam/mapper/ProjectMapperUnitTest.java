package by.teachmeskills.devteam.mapper;

import by.teachmeskills.devteam.dto.project.ProjectCardDto;
import by.teachmeskills.devteam.dto.project.ProjectDto;
import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectMapperUnitTest {

    public static final long ID_1 = 1L;
    public static final String NAME = "name";
    public static final String SPECIFICATION = "specification";
    @Mock
    private UserMapper userMapperMock;
    @Mock
    private User customerMock;
    @Mock
    private User managerMock;
    @Mock
    private User developer1Mock;
    @Mock
    private User developer2Mock;
    @Mock
    private UserDto userDtoMock;
    @InjectMocks
    private ProjectMapper projectMapper;

    @Test
    void shouldMapAllFieldsToProjectDto_whenToProjectDto_givenProjectWithAllData() {
        // given
        final var developersList = List.of(developer1Mock, developer2Mock);
        var givenProject = Project.builder()
                .id(ID_1)
                .name(NAME)
                .specification(SPECIFICATION)
                .status(ProjectStatus.NEW)
                .customer(customerMock)
                .manager(managerMock)
                .developers(developersList)
                .build();
        final var userDtoList = List.of(userDtoMock, userDtoMock);
        var expectedProjectDto = ProjectDto.builder()
                .id(ID_1)
                .name(NAME)
                .specification(SPECIFICATION)
                .status(ProjectStatus.NEW)
                .customer(userDtoMock)
                .manager(userDtoMock)
                .developers(userDtoList)
                .projectPrice(givenProject.getProjectPrice())
                .build();
        when(userMapperMock.toUserDto(any())).thenReturn(userDtoMock);
        when(userMapperMock.toUserDtoList(any())).thenReturn(userDtoList);
        // when
        var actual = projectMapper.toProjectDto(givenProject);
        // then
        verify(userMapperMock).toUserDto(customerMock);
        verify(userMapperMock).toUserDto(managerMock);
        verify(userMapperMock).toUserDtoList(developersList);
        assertThat(actual).isEqualTo(expectedProjectDto);
    }

    @Test
    void shouldMapExistingFieldsToProjectDto_whenToProjectDto_givenProjectWithMandatoryData() {
        // given
        var givenProject = Project.builder()
                .id(ID_1)
                .status(ProjectStatus.NEW)
                .build();
        var expectedProjectDto = ProjectDto.builder()
                .id(ID_1)
                .status(ProjectStatus.NEW)
                .developers(new ArrayList<>())
                .projectPrice(givenProject.getProjectPrice())
                .build();
        // when
        var actual = projectMapper.toProjectDto(givenProject);
        // then
        assertThat(actual).isEqualTo(expectedProjectDto);
    }

    @Test
    void shouldMapAllFieldsToProjectDto_whenToProjectCardDto_givenProjectWithAllData() {
        // given
        final var developersList = List.of(developer1Mock, developer2Mock);
        var givenProject = Project.builder()
                .id(ID_1)
                .name(NAME)
                .specification(SPECIFICATION)
                .status(ProjectStatus.NEW)
                .customer(customerMock)
                .manager(managerMock)
                .developers(developersList)
                .build();
        var expectedProjectDto = ProjectCardDto.builder()
                .id(ID_1)
                .name(NAME)
                .specification(SPECIFICATION)
                .status(ProjectStatus.NEW)
                .customerName(givenProject.getCustomerName())
                .managerName(givenProject.getManagerName())
                .projectPrice(givenProject.getProjectPrice())
                .build();
        // when
        var actual = projectMapper.toProjectCardDto(givenProject);
        // then
        assertThat(actual).isEqualTo(expectedProjectDto);
    }

    @Test
    void shouldMapExistingFieldsToProjectDto_whenToProjectCardDto_givenProjectWithMandatoryData() {
        // given
        var givenProject = Project.builder()
                .id(ID_1)
                .status(ProjectStatus.NEW)
                .build();
        var expectedProjectDto = ProjectCardDto.builder()
                .id(ID_1)
                .status(ProjectStatus.NEW)
                .customerName(givenProject.getCustomerName())
                .managerName(givenProject.getManagerName())
                .projectPrice(givenProject.getProjectPrice())
                .build();
        // when
        var actual = projectMapper.toProjectCardDto(givenProject);
        // then
        assertThat(actual).isEqualTo(expectedProjectDto);
    }


}