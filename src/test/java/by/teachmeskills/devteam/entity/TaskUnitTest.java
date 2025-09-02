package by.teachmeskills.devteam.entity;

import by.teachmeskills.devteam.entity.attributes.task.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskUnitTest {

    @Test
    void shouldInitializeDefaultValues_whenConstruct_givenNewTaskCreated() {
        // given / when
        var givenTask = new Task();
        // then
        assertThat(givenTask.getTime()).isZero();
        assertThat(givenTask.getPrice()).isZero();
        assertThat(givenTask.getStatus()).isEqualTo(TaskStatus.NEW);
    }

    @Test
    void shouldSetTimeValue_whenSubmitAdditionalTime_givenNewTaskCreatedAndTimeToAdd() {
        // given
        var givenTask = new Task();
        // when
        givenTask.submitAdditionalTime(1);
        // then
        assertThat(givenTask.getTime()).isEqualTo(1);
    }

    @Test
    void shouldSummarizeTimeValues_whenSubmitAdditionalTime_givenTaskWithTimeAlreadySetAndTimeToAdd() {
        // given
        Task givenTask = new Task();
        givenTask.setTime(10);
        // when
        givenTask.submitAdditionalTime(1);
        // then
        assertThat(givenTask.getTime()).isEqualTo(11);
    }

    @Test
    void shouldThrowException_whenSubmitAdditionalTime_givenTimeToAddNegativeValue() {
        // given
        var givenTask = new Task();
        // when / then
        assertThatThrownBy(() ->givenTask.submitAdditionalTime(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Additional task hours must be non-negative");
    }

}