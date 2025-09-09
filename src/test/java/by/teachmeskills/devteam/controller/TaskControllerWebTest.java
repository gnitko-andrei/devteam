package by.teachmeskills.devteam.controller;


import by.teachmeskills.devteam.config.SecurityConfiguration;
import by.teachmeskills.devteam.dto.project.ProjectDto;
import by.teachmeskills.devteam.dto.task.TaskCreationDto;
import by.teachmeskills.devteam.dto.task.TaskDto;
import by.teachmeskills.devteam.dto.task.UpdateTaskDto;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import by.teachmeskills.devteam.entity.attributes.task.TaskStatus;
import by.teachmeskills.devteam.service.ProjectService;
import by.teachmeskills.devteam.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@Import(SecurityConfiguration.class)
class TaskControllerWebTest {

    private static final long PROJECT_ID_1 = 1L;
    private static final long USER_NO_ACCESS_ID = 10L;
    private static final User USER_MANAGER = User.builder()
            .id(1L)
            .roles(Set.of(Role.USER, Role.MANAGER))
            .build();
    private static final User USER_DEVELOPER = User.builder()
            .id(2L)
            .roles(Set.of(Role.USER, Role.DEVELOPER))
            .build();
    private static final User USER_NO_ACCESS = User.builder()
            .id(USER_NO_ACCESS_ID)
            .roles(Set.of(Role.USER))
            .build();
    public static final long TASK_1_ID = 1L;
    private static final TaskDto TASK_1_DTO = TaskDto.builder().id(TASK_1_ID).status(TaskStatus.NEW).build();
    private static final TaskDto TASK_2_DTO = TaskDto.builder().id(2L).status(TaskStatus.IN_PROGRESS).build();
    private static final TaskDto TASK_3_DTO = TaskDto.builder().id(3L).status(TaskStatus.DONE).build();
    private static final ProjectDto PROJECT_DTO = ProjectDto.builder().id(PROJECT_ID_1).build();

    @MockitoBean
    private TaskService taskServiceMock;
    @MockitoBean
    private ProjectService projectServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRenderTasksList_whenGetProjectsTasks_givenProjectId() throws Exception {
        // given
        var expectedTasks = List.of(TASK_1_DTO, TASK_2_DTO, TASK_3_DTO);
        var expectedStatusFilter = TaskStatus.IN_PROGRESS;
        when(taskServiceMock.findByProjectIdAndStatus(any(), any())).thenReturn(expectedTasks);
        when(projectServiceMock.findById(any())).thenReturn(PROJECT_DTO);
        // when / then
        mockMvc.perform(get("/projects/{projectId}/tasks", PROJECT_ID_1).with(user(USER_MANAGER))
                        .param("statusFilter", expectedStatusFilter.name()))
                .andExpect(status().isOk())
                .andExpect(view().name("tasksList"))
                .andExpect(model().attribute("taskStatuses", TaskStatus.values()))
                .andExpect(model().attribute("project", PROJECT_DTO))
                .andExpect(model().attribute("tasks", expectedTasks))
                .andExpect(model().attribute("statusFilter", expectedStatusFilter));
        verify(taskServiceMock).findByProjectIdAndStatus(PROJECT_ID_1, expectedStatusFilter);
        verify(projectServiceMock).findById(PROJECT_ID_1);
    }

    @Test
    void shouldForbidAccess_whenGetProjects_givenNotAuthorisedUserRoles() throws Exception {
        // given
        // when / then
        mockMvc.perform(get("/projects/{projectId}/tasks", PROJECT_ID_1).with(user(USER_NO_ACCESS)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(projectServiceMock);
        verifyNoInteractions(taskServiceMock);
    }

    @Test
    void shouldCreateNewTaskAdnRedirectToTaskList_whenPostProjectTasks_givenTaskCreationData() throws Exception {
        // given
        var givenName = "name";
        var givenDescription = "description";
        var expectedTaskCreationDto = TaskCreationDto.builder()
                .projectId(PROJECT_ID_1)
                .name(givenName)
                .description(givenDescription)
                .build();
        // when / then
        mockMvc.perform(post("/projects/{projectId}/tasks", PROJECT_ID_1).with(csrf())
                        .with(user(USER_MANAGER))
                        .param("name", givenName)
                        .param("description", givenDescription)
                        .param("projectId", String.valueOf(PROJECT_ID_1)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1/tasks"));
        verify(taskServiceMock).addNewTask(expectedTaskCreationDto);
    }

    @Test
    void shouldReturnForbidden_whenPostProjectTasks_withoutCsrf() throws Exception {
        mockMvc.perform(post("/projects/{projectId}/tasks", PROJECT_ID_1).with(user(USER_MANAGER)))
                .andExpect(status().isForbidden());
        verifyNoInteractions(taskServiceMock);
    }

    @Test
    void shouldUpdateTaskAdnRedirectToTaskList_whenPostProjectTasksTaskId_givenUpdateTaskData() throws Exception {
        // given
        var givenSubmittedTime = 10;
        var expectedUpdateTaskDto = UpdateTaskDto.builder()
                .id(TASK_1_ID)
                .submittedTime(givenSubmittedTime)
                .status(TaskStatus.IN_PROGRESS)
                .build();
        // when / then
        mockMvc.perform(post("/projects/{projectId}/tasks/{taskId}", PROJECT_ID_1, TASK_1_ID).with(csrf())
                        .with(user(USER_DEVELOPER))
                        .param("submittedTime", String.valueOf(givenSubmittedTime))
                        .param("status", TaskStatus.IN_PROGRESS.name())
                        .param("taskId", String.valueOf(TASK_1_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1/tasks"));
        verify(taskServiceMock).updateTask(expectedUpdateTaskDto);
    }

    @Test
    void shouldDeleteTaskAdnRedirectToTaskList_whenDeleteProjectTasks_givenTaskId() throws Exception {
        // given
        // when / then
        mockMvc.perform(delete("/projects/{projectId}/tasks", PROJECT_ID_1).with(csrf())
                        .with(user(USER_MANAGER))
                        .param("id", String.valueOf(TASK_1_ID)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1/tasks"));
        verify(taskServiceMock).deleteById(TASK_1_ID);
    }
}