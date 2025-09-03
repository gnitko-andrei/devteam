package by.teachmeskills.devteam.mapper;

import by.teachmeskills.devteam.dto.task.TaskCreationDto;
import by.teachmeskills.devteam.dto.task.TaskDto;
import by.teachmeskills.devteam.entity.Task;
import by.teachmeskills.devteam.entity.attributes.task.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperUnitTest {

    public static final long ID_1 = 1L;
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final int TIME = 100;
    public static final int PRICE = 1000;

    @Test
    void shouldMapAllFieldsToTaskDto_whenToTaskDto_givenTaskEntityWithAllData() {
        // given
        var mapper = new TaskMapper();
        var givenTaskEntity = Task.builder()
                .id(ID_1)
                .name(NAME)
                .description(DESCRIPTION)
                .time(TIME)
                .price(PRICE)
                .status(TaskStatus.NEW)
                .build();
        var expectedTaskDto = TaskDto.builder()
                .id(ID_1)
                .name(NAME)
                .description(DESCRIPTION)
                .time(TIME)
                .price(PRICE)
                .status(TaskStatus.NEW)
                .build();
        // when
        var actual = mapper.toTaskDto(givenTaskEntity);
        // then
        assertThat(actual).isEqualTo(expectedTaskDto);
    }

    @Test
    void shouldMapAllExistingFieldsToTaskDto_whenToTaskDto_givenTaskEntityWithMandatoryData() {
        // given
        var mapper = new TaskMapper();
        var givenTaskEntity = Task.builder()
                .id(ID_1)
                .status(TaskStatus.NEW)
                .build();
        var expectedTaskDto = TaskDto.builder()
                .id(ID_1)
                .status(TaskStatus.NEW)
                .build();
        // when
        var actual = mapper.toTaskDto(givenTaskEntity);
        // then
        assertThat(actual).isEqualTo(expectedTaskDto);
    }

    @Test
    void shouldMapAllFieldsToTaskEntity_whenToTaskDto_givenTaskCreationDto() {
        // given
        var mapper = new TaskMapper();
        var givenTaskCreationDto = TaskCreationDto.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .build();
        var expectedTaskEntity = Task.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .build();
        // when
        var actual = mapper.toEntity(givenTaskCreationDto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedTaskEntity);
    }


}