package by.teachmeskills.devteam.controller;

import by.teachmeskills.devteam.config.SecurityConfiguration;
import by.teachmeskills.devteam.dto.project.*;
import by.teachmeskills.devteam.dto.user.UserDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import by.teachmeskills.devteam.service.ProjectService;
import by.teachmeskills.devteam.service.UserService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("web")
@WebMvcTest(ProjectController.class)
@Import(SecurityConfiguration.class)
class ProjectControllerWebTest {

    private static final long USER_CUSTOMER_ID = 1L;
    private static final long USER_MANAGER_ID = 2L;
    private static final long USER_DEVELOPER_1_ID = 21L;
    private static final long USER_DEVELOPER_2_ID = 22L;
    private static final long USER_NO_ACCESS_ID = 10L;
    private static final Set<Role> USER_CUSTOMER_ROLES = Set.of(Role.USER, Role.CUSTOMER);
    private static final Set<Role> USER_MANAGER_ROLES = Set.of(Role.USER, Role.MANAGER);
    private static final User USER_CUSTOMER = User.builder()
            .id(USER_CUSTOMER_ID)
            .roles(USER_CUSTOMER_ROLES)
            .build();
    private static final User USER_MANAGER = User.builder()
            .id(USER_MANAGER_ID)
            .roles(USER_MANAGER_ROLES)
            .build();
    private static final User USER_NO_ACCESS = User.builder()
            .id(USER_NO_ACCESS_ID)
            .roles(Set.of(Role.USER))
            .build();
    private static final UserDto USER_DTO_1 = UserDto.builder().id(1L).build();
    private static final UserDto USER_DTO_2 = UserDto.builder().id(2L).build();
    private static final UserDto USER_DTO_3 = UserDto.builder().id(3L).build();
    private static final long PROJECT_ID_1 = 1L;
    private static final ProjectCardDto PROJECT_CARD_DTO_1 = ProjectCardDto.builder().id(PROJECT_ID_1).status(ProjectStatus.NEW).build();
    private static final ProjectCardDto PROJECT_CARD_DTO_2 = ProjectCardDto.builder().id(2L).status(ProjectStatus.NEW).build();
    private static final ProjectCardDto PROJECT_CARD_DTO_3 = ProjectCardDto.builder().id(3L).status(ProjectStatus.NEW).build();
    private static final ProjectDto PROJECT_DTO_1 = ProjectDto.builder()
            .id(PROJECT_ID_1)
            .name("project1")
            .status(ProjectStatus.NEW)
            .specification("specification")
            .customer(USER_DTO_1)
            .manager(USER_DTO_2)
            .developers(List.of())
            .build();

    @MockitoBean
    private ProjectService projectServiceMock;
    @MockitoBean
    private UserService userServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderProjects_whenGetProjects() throws Exception {
        // given
        var givenNameFilter = "nameFilter";
        var givenStatusFilter = ProjectStatus.NEW;
        var expectedProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(USER_CUSTOMER_ID)
                .userRoles(USER_CUSTOMER_ROLES)
                .nameFilter(givenNameFilter)
                .statusFilter(givenStatusFilter)
                .build();
        var projects = List.of(PROJECT_CARD_DTO_1, PROJECT_CARD_DTO_2, PROJECT_CARD_DTO_3);
        when(projectServiceMock.findUserProjects(any())).thenReturn(projects);
        var managers = List.of(USER_DTO_1, USER_DTO_2, USER_DTO_3);
        when(userServiceMock.getAllUsersByRole(any())).thenReturn(managers);
        // when / then
        mockMvc.perform(get("/projects").with(user(USER_CUSTOMER))
                        .param("nameFilter", givenNameFilter)
                        .param("statusFilter", givenStatusFilter.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("projects"))
                .andExpect(model().attribute("projectStatuses", ProjectStatus.visibleForFiltering()))
                .andExpect(model().attribute("userProjectsList", projects))
                .andExpect(model().attribute("nameFilter", expectedProjectFiltersDto.getNameFilter()))
                .andExpect(model().attribute("statusFilter", expectedProjectFiltersDto.getStatusFilter()))
                .andExpect(model().attribute("managers", managers));
        verify(projectServiceMock).findUserProjects(expectedProjectFiltersDto);
        verify(userServiceMock).getAllUsersByRole(Role.MANAGER);
    }

    @Test
    void shouldForbidAccess_whenGetProjects_givenNotAuthorisedUserRoles() throws Exception {
        // given
        // when / then
        mockMvc.perform(get("/projects").with(user(USER_NO_ACCESS)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(projectServiceMock);
        verifyNoInteractions(userServiceMock);
    }

    @Test
    void shouldCreateNewProjectAndRedirectToProjects_whenPostProjects_givenCustomerUserAndNewProjectData() throws Exception {
        // given
        var givenNewProjectName = "newProjectName";
        var givenNewProjectSpecification = "newProjectSpecification";
        var givenManagerId = 3L;
        var expectedProjectCreationDto = ProjectCreationDto.builder()
                .newProjectName(givenNewProjectName)
                .newProjectSpecification(givenNewProjectSpecification)
                .managerId(givenManagerId)
                .customerId(USER_CUSTOMER.getId())
                .build();
        // when / then
        mockMvc.perform(post("/projects").with(csrf()).with(user(USER_CUSTOMER))
                        .param("newProjectName", givenNewProjectName)
                        .param("newProjectSpecification", givenNewProjectSpecification)
                        .param("managerId", String.valueOf(givenManagerId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
        verify(projectServiceMock).addNewProject(expectedProjectCreationDto);
    }

    @Test
    void shouldForbidAccess_whenPostProjects_givenNotCustomerUserRoles() throws Exception {
        // given
        // when / then
        mockMvc.perform(post("/projects").with(csrf()).with(user(USER_MANAGER)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(projectServiceMock);
        verifyNoInteractions(userServiceMock);
    }

    @Test
    void shouldRenderProjectInfo_whenGetProjectsProjectId_givenProjectId() throws Exception {
        // given
        when(projectServiceMock.findById(any())).thenReturn(PROJECT_DTO_1);
        // when / then
        mockMvc.perform(get("/projects/{projectId}", PROJECT_ID_1).with(user(USER_CUSTOMER)))
                .andExpect(status().isOk())
                .andExpect(view().name("projectInfo"))
                .andExpect(model().attribute("project", PROJECT_DTO_1));
        verify(projectServiceMock).findById(PROJECT_ID_1);
    }

    @Test
    void shouldRenderProjectEditor_whenGetEditProjectId_givenProjectIdAndAuthorisedUserRole() throws Exception {
        // given
        when(projectServiceMock.findById(any())).thenReturn(PROJECT_DTO_1);
        var expectedManagers = List.of(USER_DTO_1);
        when(userServiceMock.getAllUsersByRole(Role.MANAGER)).thenReturn(expectedManagers);
        var expectedDevelopers = List.of(USER_DTO_2, USER_DTO_3);
        when(userServiceMock.getAllUsersByRole(Role.DEVELOPER)).thenReturn(expectedDevelopers);
        // when / then
        mockMvc.perform(get("/projects/edit/{projectId}", PROJECT_ID_1).with(user(USER_CUSTOMER)))
                .andExpect(status().isOk())
                .andExpect(view().name("projectEditor"))
                .andExpect(model().attribute("project", PROJECT_DTO_1))
                .andExpect(model().attribute("statuses", ProjectStatus.visibleForFiltering()))
                .andExpect(model().attribute("managers", expectedManagers))
                .andExpect(model().attribute("developers", expectedDevelopers));
        verify(projectServiceMock).findById(PROJECT_ID_1);
        var roleCaptor = ArgumentCaptor.forClass(Role.class);
        verify(userServiceMock, times(2)).getAllUsersByRole(roleCaptor.capture());
        assertThat(roleCaptor.getAllValues()).containsExactly(Role.MANAGER, Role.DEVELOPER);
    }

    @Test
    void shouldUpdateProjectAndRedirectToProjectInfo_whenPostEditProjectId_givenProjectIdAndUpdateProjectData() throws Exception {
        // given
        final var givenName = "name";
        final var givenSpecification = "specification";
        var expectedUpdateProjectDto = UpdateProjectDto.builder()
                .id(PROJECT_ID_1)
                .name(givenName)
                .specification(givenSpecification)
                .status(ProjectStatus.IN_PROGRESS)
                .managerId(USER_MANAGER_ID)
                .developerIds(List.of(USER_DEVELOPER_1_ID, USER_DEVELOPER_2_ID))
                .build();
        // when / then
        mockMvc.perform(post("/projects/edit/{projectId}", PROJECT_ID_1).with(csrf()).with(user(USER_CUSTOMER))
                        .param("name", givenName)
                        .param("specification", givenSpecification)
                        .param("status", ProjectStatus.IN_PROGRESS.name())
                        .param("managerId", String.valueOf(USER_MANAGER_ID))
                        .param("developerIds", String.valueOf(USER_DEVELOPER_1_ID), String.valueOf(USER_DEVELOPER_2_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
        verify(projectServiceMock).updateProject(USER_CUSTOMER.getRoles(), expectedUpdateProjectDto);
    }

    @Test
    @WithMockUser(authorities = "MANAGER")
    void shouldDeleteProjectAndRedirectToProjects_whenDeleteProject_givenProjectId() throws Exception {
        // given
        // when / then
        mockMvc.perform(delete("/projects").with(csrf())
                        .param("projectId", String.valueOf(PROJECT_ID_1)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
        verify(projectServiceMock).deleteById(PROJECT_ID_1);
    }
}