package by.teachmeskills.devteam.service;

import by.teachmeskills.devteam.dto.task.TaskCreationDto;
import by.teachmeskills.devteam.dto.task.TaskDto;
import by.teachmeskills.devteam.dto.task.UpdateTaskDto;
import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.Task;
import by.teachmeskills.devteam.entity.attributes.task.TaskStatus;
import by.teachmeskills.devteam.exception.ProjectNotFoundException;
import by.teachmeskills.devteam.exception.TaskNotFoundException;
import by.teachmeskills.devteam.mapper.TaskMapper;
import by.teachmeskills.devteam.repository.ProjectRepository;
import by.teachmeskills.devteam.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceUnitTest {

    public static final long PROJECT_ID = 10L;
    public static final long TASK_ID = 1L;
    public static final int SUBMITTED_TIME = 10;

    @Mock
    private ProjectRepository projectRepositoryMock;
    @Mock
    private TaskRepository taskRepositoryMock;
    @Mock
    private TaskMapper taskMapperMock;
    @Mock
    private Task taskMock;
    @Mock
    private Task taskMock1;
    @Mock
    private Task taskMock2;
    @Mock
    private TaskDto taskDtoMock;
    @Mock
    private TaskDto taskDtoMock1;
    @Mock
    private TaskDto taskDtoMock2;
    @Mock
    private Project projectMock;
    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldReturnListOfTestDto_whenFindByProjectIdAndStatus_givenProjectIdAndTaskStatus() {
        // given
        when(taskRepositoryMock.findByProjectIdAndStatus(any(), any())).thenReturn(List.of(taskMock, taskMock1, taskMock2));
        when(taskMapperMock.toTaskDto(any())).thenReturn(taskDtoMock, taskDtoMock1, taskDtoMock2);
        // when
        var actual = taskService.findByProjectIdAndStatus(PROJECT_ID, TaskStatus.NEW);
        // then
        verify(taskRepositoryMock).findByProjectIdAndStatus(PROJECT_ID, TaskStatus.NEW);
        var taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskMapperMock, times(3)).toTaskDto(taskCaptor.capture());
        assertThat(taskCaptor.getAllValues()).containsExactly(taskMock, taskMock1, taskMock2);
        assertThat(actual).containsExactly(taskDtoMock, taskDtoMock1, taskDtoMock2);
    }

    @Test
    void shouldReturnEmptyList_whenFindByProjectIdAndStatus_givenProjectIdAndTaskStatusAndNothingFound() {
        // given
        when(taskRepositoryMock.findByProjectIdAndStatus(any(), any())).thenReturn(List.of());
        // when
        var actual = taskService.findByProjectIdAndStatus(PROJECT_ID, TaskStatus.NEW);
        // then
        verify(taskRepositoryMock).findByProjectIdAndStatus(PROJECT_ID, TaskStatus.NEW);
        verify(taskMapperMock, never()).toTaskDto(any());
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldSaveNewTask_whenAddNewTask_givenTaskCreationDto() {
        // given
        var givenTaskCreationDto = TaskCreationDto.builder()
                .projectId(PROJECT_ID)
                .build();
        when(projectRepositoryMock.findById(any())).thenReturn(Optional.of(projectMock));
        when(taskMapperMock.toEntity(any())).thenReturn(taskMock);
        // when
        taskService.addNewTask(givenTaskCreationDto);
        // then
        verify(projectRepositoryMock).findById(PROJECT_ID);
        verify(taskMapperMock).toEntity(givenTaskCreationDto);
        verify(taskMock).setProject(projectMock);
        verify(taskRepositoryMock).save(taskMock);
    }

    @Test
    void shouldThrowProjectNotFoundException_whenAddNewTask_givenTaskCreationDtoAndNotExistingProjectId() {
        // given
        var givenTaskCreationDto = TaskCreationDto.builder()
                .projectId(PROJECT_ID)
                .build();
        when(projectRepositoryMock.findById(any())).thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> taskService.addNewTask(givenTaskCreationDto))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage("Project not found. Project Id: " + PROJECT_ID);
        // then
        verify(projectRepositoryMock).findById(PROJECT_ID);
        verify(taskMapperMock, never()).toEntity(any());
        verify(taskRepositoryMock, never()).save(any());
    }

    @Test
    void shouldUpdateTaskData_whenUpdateTask_givenUpdateTaskDtoWithAllUpdatedData() {
        // given
        var givenUpdaateTaskDto = UpdateTaskDto.builder()
                .id(TASK_ID)
                .status(TaskStatus.IN_PROGRESS)
                .submittedTime(SUBMITTED_TIME)
                .build();
        when(taskRepositoryMock.findById(any())).thenReturn(Optional.of(taskMock));
        // when
        taskService.updateTask(givenUpdaateTaskDto);
        // then
        verify(taskRepositoryMock).findById(TASK_ID);
        verify(taskMock).setStatus(TaskStatus.IN_PROGRESS);
        verify(taskMock).submitAdditionalTime(SUBMITTED_TIME);
        verify(taskRepositoryMock).save(taskMock);
    }

    @Test
    void shouldUpdateTaskExceptMissingFields_whenUpdateTask_givenUpdateTaskDtoWithIdOnly() {
        // given
        var givenUpdaateTaskDto = UpdateTaskDto.builder()
                .id(TASK_ID)
                .build();
        when(taskRepositoryMock.findById(any())).thenReturn(Optional.of(taskMock));
        // when
        taskService.updateTask(givenUpdaateTaskDto);
        // then
        verify(taskRepositoryMock).findById(TASK_ID);
        verify(taskMock, never()).setStatus(any());
        verify(taskMock, never()).submitAdditionalTime(anyInt());
        verify(taskRepositoryMock).save(taskMock);
    }

    @Test
    void shouldThrowTaskNotFoundException_whenUpdateTask_givenUpdateTaskDtoWithIdOfNonExistingTask() {
        // given
        var givenUpdaateTaskDto = UpdateTaskDto.builder()
                .id(TASK_ID)
                .build();
        when(taskRepositoryMock.findById(any())).thenReturn(Optional.empty());
        // when / then
        assertThatThrownBy(() -> taskService.updateTask(givenUpdaateTaskDto))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found. Task Id: " + TASK_ID);
        verify(taskRepositoryMock).findById(TASK_ID);
        verify(taskRepositoryMock, never()).save(taskMock);
    }

    @Test
    void shouldDeleteTask_whenDeleteById_givenTaskId() {
        // given
        // when
        taskService.deleteById(TASK_ID);
        // then
        taskRepositoryMock.deleteById(TASK_ID);
    }

}