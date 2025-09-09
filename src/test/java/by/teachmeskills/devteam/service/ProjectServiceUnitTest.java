package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.dto.project.*;
import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import by.teachmeskills.devteam.exception.ProjectNameAlreadyInUseException;
import by.teachmeskills.devteam.exception.ProjectNotFoundException;
import by.teachmeskills.devteam.exception.UserNotFoundException;
import by.teachmeskills.devteam.mapper.ProjectMapper;
import by.teachmeskills.devteam.repository.ProjectRepository;
import by.teachmeskills.devteam.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceUnitTest {

    public static final long PROJECT_ID = 1L;
    public static final long MANAGER_ID = 10L;
    public static final long CUSTOMER_ID = 11L;
    public static final long DEVELOPER_ID_1 = 111L;
    public static final long DEVELOPER_ID_2 = 112;
    public static final long DEVELOPER_ID_3 = 113;
    public static final String  PROJECT_NAME = "projectName";
    public static final String  PROJECT_SPECIFICATION = "specification";
    @Mock
    private ProjectRepository projectRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private ProjectMapper projectMapperMock;
    @Mock
    private Project projectMock;
    @Mock
    private Project projectMock1;
    @Mock
    private Project projectMock2;
    @Mock
    private ProjectDto projectDtoMock;
    @Mock
    private ProjectCardDto projectCardDtoMock;
    @Mock
    private ProjectCardDto projectCardDtoMock1;
    @Mock
    private ProjectCardDto projectCardDtoMock2;
    @Mock
    private User userMock1;
    @Mock
    private User userMock2;
    @Mock
    private User userMock3;
    @InjectMocks
    private ProjectService projectService;

    @Test
    void shouldProjectDto_whenFindById_givenExistingProjectId() {
        // given
        when(projectRepositoryMock.findById(any())).thenReturn(Optional.of(projectMock));
        when(projectMapperMock.toProjectDto(any())).thenReturn(projectDtoMock);
        // when
        projectService.findById(PROJECT_ID);
        // then
        verify(projectRepositoryMock).findById(PROJECT_ID);
        verify(projectMapperMock).toProjectDto(projectMock);
    }

    @Test
    void shouldThrowProjectNotFoundException_whenFindById_givenNotExistingProjectId() {
        // given
        when(projectRepositoryMock.findById(any())).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> projectService.findById(PROJECT_ID))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage("Project not found. Project Id: " + PROJECT_ID);
        verify(projectRepositoryMock).findById(PROJECT_ID);
        verify(projectMapperMock, never()).toProjectDto(any());
    }

    @Test
    void shouldFindCustomerProjects_whenFindUserProjects_givenProjectFiltersForCustomer() {
        // given
        var givenFilters = ProjectFiltersDto.builder()
                .userRoles(Set.of(Role.USER, Role.CUSTOMER))
                .build();
        when(projectRepositoryMock.findCustomerProjects(any())).thenReturn(List.of(projectMock, projectMock1, projectMock2));
        when(projectMapperMock.toProjectCardDto(any())).thenReturn(projectCardDtoMock, projectCardDtoMock1, projectCardDtoMock2);
        // when
        var actual = projectService.findUserProjects(givenFilters);
        // then
        verify(projectRepositoryMock).findCustomerProjects(givenFilters);
        var projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectMapperMock, times(3)).toProjectCardDto(projectCaptor.capture());
        assertThat(projectCaptor.getAllValues()).containsExactly(projectMock, projectMock1, projectMock2);
        assertThat(actual).containsExactly(projectCardDtoMock, projectCardDtoMock1, projectCardDtoMock2);
    }

    @Test
    void shouldFindManagerProjects_whenFindUserProjects_givenProjectFiltersForManager() {
        // given
        var givenFilters = ProjectFiltersDto.builder()
                .userRoles(Set.of(Role.USER, Role.MANAGER))
                .build();
        when(projectRepositoryMock.findManagerProjects(any())).thenReturn(List.of(projectMock, projectMock1, projectMock2));
        when(projectMapperMock.toProjectCardDto(any())).thenReturn(projectCardDtoMock, projectCardDtoMock1, projectCardDtoMock2);
        // when
        var actual = projectService.findUserProjects(givenFilters);
        // then
        verify(projectRepositoryMock).findManagerProjects(givenFilters);
        var projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectMapperMock, times(3)).toProjectCardDto(projectCaptor.capture());
        assertThat(projectCaptor.getAllValues()).containsExactly(projectMock, projectMock1, projectMock2);
        assertThat(actual).containsExactly(projectCardDtoMock, projectCardDtoMock1, projectCardDtoMock2);
    }

    @Test
    void shouldFindDeveloperProjects_whenFindUserProjects_givenProjectFiltersForDeveloper() {
        // given
        var givenFilters = ProjectFiltersDto.builder()
                .userRoles(Set.of(Role.USER, Role.DEVELOPER))
                .build();
        when(projectRepositoryMock.findDeveloperProjects(any())).thenReturn(List.of(projectMock, projectMock1, projectMock2));
        when(projectMapperMock.toProjectCardDto(any())).thenReturn(projectCardDtoMock, projectCardDtoMock1, projectCardDtoMock2);
        // when
        var actual = projectService.findUserProjects(givenFilters);
        // then
        verify(projectRepositoryMock).findDeveloperProjects(givenFilters);
        var projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(projectMapperMock, times(3)).toProjectCardDto(projectCaptor.capture());
        assertThat(projectCaptor.getAllValues()).containsExactly(projectMock, projectMock1, projectMock2);
        assertThat(actual).containsExactly(projectCardDtoMock, projectCardDtoMock1, projectCardDtoMock2);
    }

    @Test
    void shouldFindDeveloperProjects_whenFindUserProjects_givenProjectFiltersForUSerWithoutBusinessRole() {
        // given
        var givenFilters = ProjectFiltersDto.builder()
                .userRoles(Set.of(Role.USER))
                .build();
        // when
        var actual = projectService.findUserProjects(givenFilters);
        // then
        verifyNoInteractions(projectRepositoryMock);
        verifyNoInteractions(projectMapperMock);
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldSaveNewProject_whenAddNewProject_givenProjectCreationDto() {
        // given
        var givenProjectCreationDto = ProjectCreationDto.builder()
                .newProjectName(PROJECT_NAME)
                .managerId(MANAGER_ID)
                .customerId(CUSTOMER_ID)
                .newProjectSpecification(PROJECT_SPECIFICATION)
                .build();
        when(projectRepositoryMock.findByName(any())).thenReturn(Optional.empty());
        when(userRepositoryMock.findById(MANAGER_ID)).thenReturn(Optional.of(userMock1));
        when(userRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.of(userMock2));
        var expectedProject = Project.builder()
                .name(PROJECT_NAME)
                .specification(PROJECT_SPECIFICATION)
                .status(ProjectStatus.NEW)
                .manager(userMock1)
                .customer(userMock2)
                .build();
        // when
        projectService.addNewProject(givenProjectCreationDto);
        // then
        verify(projectRepositoryMock).findByName(PROJECT_NAME);
        verify(userRepositoryMock).findById(MANAGER_ID);
        verify(userRepositoryMock).findById(CUSTOMER_ID);
        verify(projectRepositoryMock).save(expectedProject);
    }

    @Test
    void shouldThrowProjectNameAlreadyInUseException_whenAddNewProject_givenProjectCreationDtoWithAlreadyExistingProjectName() {
        // given
        var givenProjectCreationDto = ProjectCreationDto.builder()
                .newProjectName(PROJECT_NAME)
                .build();
        when(projectRepositoryMock.findByName(any())).thenReturn(Optional.of(projectMock));
        // when / then
        assertThatThrownBy(() -> projectService.addNewProject(givenProjectCreationDto))
                .isInstanceOf(ProjectNameAlreadyInUseException.class)
                .hasMessage("Cannot create/update the project. Project name '%s' is already in use".formatted(PROJECT_NAME));
        verify(projectRepositoryMock).findByName(PROJECT_NAME);
        verify(projectRepositoryMock, never()).save(any());
    }

    @Test
    void shouldThrowUserNotFoundException_whenAddNewProject_givenProjectCreationDtoWithWrongManagerId() {
        // given
        var givenProjectCreationDto = ProjectCreationDto.builder()
                .newProjectName(PROJECT_NAME)
                .managerId(MANAGER_ID)
                .build();
        when(projectRepositoryMock.findByName(any())).thenReturn(Optional.empty());
        when(userRepositoryMock.findById(MANAGER_ID)).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> projectService.addNewProject(givenProjectCreationDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found. Id: " + MANAGER_ID);
        verify(projectRepositoryMock).findByName(PROJECT_NAME);
        verify(userRepositoryMock).findById(MANAGER_ID);
        verify(projectRepositoryMock, never()).save(any());
    }

    @Test
    void shouldThrowUserNotFoundException_whenAddNewProject_givenProjectCreationDtoWrongCustomerId() {
        // given
        var givenProjectCreationDto = ProjectCreationDto.builder()
                .newProjectName(PROJECT_NAME)
                .managerId(MANAGER_ID)
                .customerId(CUSTOMER_ID)
                .build();
        when(projectRepositoryMock.findByName(any())).thenReturn(Optional.empty());
        when(userRepositoryMock.findById(MANAGER_ID)).thenReturn(Optional.of(userMock1));
        when(userRepositoryMock.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> projectService.addNewProject(givenProjectCreationDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found. Id: " + CUSTOMER_ID);
        verify(projectRepositoryMock).findByName(PROJECT_NAME);
        verify(userRepositoryMock).findById(MANAGER_ID);
        verify(userRepositoryMock).findById(CUSTOMER_ID);
        verify(projectRepositoryMock, never()).save(any());

    }

    @Test
    void shouldUpdateFieldsEditableForCustomer_whenUpdateProject_givenUpdateProjectDtoAndUserRoleCustomer() {
        // given
        var givenUserRoles = Set.of(Role.USER, Role.CUSTOMER);
        var givenUpdateProjectDto = UpdateProjectDto.builder()
                .id(PROJECT_ID)
                .name(PROJECT_NAME)
                .specification(PROJECT_SPECIFICATION)
                .managerId(MANAGER_ID)
                .build();
        when(projectRepositoryMock.findById(any())).thenReturn(Optional.of(projectMock));
        when(userRepositoryMock.findById(MANAGER_ID)).thenReturn(Optional.of(userMock1));
        // when
        projectService.updateProject(givenUserRoles, givenUpdateProjectDto);
        // then
        verify(projectRepositoryMock).findById(PROJECT_ID);
        verify(userRepositoryMock).findById(MANAGER_ID);
        verify(projectMock).setName(PROJECT_NAME);
        verify(projectMock).setSpecification(PROJECT_SPECIFICATION);
        verify(projectMock).setManager(userMock1);
        verify(projectRepositoryMock).save(projectMock);
    }

    @Test
    void shouldThrowUserNotFoundException_whenUpdateProject_givenUpdateProjectDtoAndUserRoleCustomerAndNotExistingManagerId() {
        // given
        var givenUserRoles = Set.of(Role.USER, Role.CUSTOMER);
        var givenUpdateProjectDto = UpdateProjectDto.builder()
                .id(PROJECT_ID)
                .managerId(MANAGER_ID)
                .build();
        when(projectRepositoryMock.findById(any())).thenReturn(Optional.of(projectMock));
        when(userRepositoryMock.findById(MANAGER_ID)).thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> projectService.updateProject(givenUserRoles, givenUpdateProjectDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found. Id: " + MANAGER_ID);
        // then
        verify(projectRepositoryMock).findById(PROJECT_ID);
        verify(projectRepositoryMock, never()).save(any());

    }

    @Test
    void shouldUpdateFieldsEditableForManager_whenUpdateProject_givenUpdateProjectDtoAndUserRoleManager() {
        // given
        var givenUserRoles = Set.of(Role.USER, Role.MANAGER);
        var givenUpdateProjectDto = UpdateProjectDto.builder()
                .id(PROJECT_ID)
                .status(ProjectStatus.COMPLETED)
                .developerIds(List.of(DEVELOPER_ID_1, DEVELOPER_ID_2, DEVELOPER_ID_3))
                .build();
        when(projectRepositoryMock.findById(any())).thenReturn(Optional.of(projectMock));
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(userMock1))
                .thenReturn(Optional.of(userMock2))
                .thenReturn(Optional.of(userMock3));
        // when
        projectService.updateProject(givenUserRoles, givenUpdateProjectDto);
        // then
        verify(projectRepositoryMock).findById(PROJECT_ID);
        var userIdCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepositoryMock, times(3)).findById(userIdCaptor.capture());
        assertThat(userIdCaptor.getAllValues()).containsExactly(DEVELOPER_ID_1, DEVELOPER_ID_2, DEVELOPER_ID_3);
        verify(projectMock).setStatus(ProjectStatus.COMPLETED);
        verify(projectMock).setDevelopers(List.of(userMock1, userMock2, userMock3));
    }

    @Test
    void shouldSetDevelopersAsEmptyList_whenUpdateProject_givenUpdateProjectDtoWithNullDeveloperIdsAndUserRoleManager() {
        // given
        var givenUserRoles = Set.of(Role.USER, Role.MANAGER);
        var givenUpdateProjectDto = UpdateProjectDto.builder()
                .id(PROJECT_ID)
                .status(ProjectStatus.COMPLETED)
                .build();
        when(projectRepositoryMock.findById(any())).thenReturn(Optional.of(projectMock));
        // when
        projectService.updateProject(givenUserRoles, givenUpdateProjectDto);
        // then
        verify(projectRepositoryMock).findById(PROJECT_ID);
        verify(projectMock).setStatus(ProjectStatus.COMPLETED);
        verify(projectMock).setDevelopers(List.of());
    }

    @Test
    void shouldThrowProjectNotFoundException_whenUpdateProject_givenUpdateProjectDtoAndNotExistingProjectId() {
        // given
        var givenUserRoles = Set.of(Role.USER, Role.MANAGER);
        var givenUpdateProjectDto = UpdateProjectDto.builder()
                .id(PROJECT_ID)
                .build();
        when(projectRepositoryMock.findById(any())).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> projectService.updateProject(givenUserRoles, givenUpdateProjectDto))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage("Project not found. Project Id: " + PROJECT_ID);
        verify(projectRepositoryMock).findById(PROJECT_ID);
    }

    @Test
    void shouldDeleteProject_whenDeleteById_givenProjectId() {
        // given
        // when
        projectService.deleteById(PROJECT_ID);
        // then
        verify(projectRepositoryMock).deleteById(PROJECT_ID);
    }

}