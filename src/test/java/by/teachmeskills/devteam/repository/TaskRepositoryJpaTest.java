package by.teachmeskills.devteam.repository;

import by.teachmeskills.devteam.common.AbstractJpaTest;
import by.teachmeskills.devteam.entity.Task;
import by.teachmeskills.devteam.entity.attributes.task.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/testdata/jpa/taskRepositoryTestData.sql")
class TaskRepositoryJpaTest extends AbstractJpaTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldReturnTaskEntityWithCorrectlyMappedData_whenFindById_givenTaskId() {
        // given
        var givenProjectId = 2000L;
        var expectedDescription = """
                As a business analyst, I need to conduct interviews, collect user stories, and document them in Confluence so that the team understands the scope.
                
                Acceptance criteria:
                - At least 5 stakeholder interviews completed
                - User stories documented and approved by customer
                
                Additional notes:
                - Use Miro for workflow diagrams""";
        // when
        var actual = taskRepository.findById(givenProjectId);
        // then
        assertThat(actual).isPresent().get().satisfies(task -> {
            assertThat(task.getId()).isEqualTo(givenProjectId);
            assertThat(task.getName()).isEqualTo("Gather requirements");
            assertThat(task.getStatus()).isEqualTo(TaskStatus.NEW);
            assertThat(task.getDescription()).isEqualTo(expectedDescription);
            assertThat(task.getTime()).isEqualTo(5);
            assertThat(task.getPrice()).isEqualTo(520);
            assertThat(task.getProject().getId()).isEqualTo(100L);
        });
    }

    @Test
    void shouldReturnListOfProjectTasks_whenFindByProjectIdAndStatus_givenExistingProjectIdAndNoStatusFilter() {
        // given / when
        var actual = taskRepository.findByProjectIdAndStatus(100L, null);
        // then
        assertThat(actual).hasSize(7).extracting(Task::getId)
                .containsExactlyInAnyOrder(2000L, 2001L, 2002L, 2003L, 2004L, 2005L, 2006L);

    }

    @Test
    void shouldReturnListOfProjectTasksFilteredByStatus_whenFindByProjectIdAndStatus_givenExistingProjectIdAndStatusFilter() {
        // given / when
        var actual = taskRepository.findByProjectIdAndStatus(100L, TaskStatus.NEW);
        // then
        assertThat(actual).hasSize(4).extracting(Task::getId)
                .containsExactlyInAnyOrder(2000L, 2001L, 2005L, 2006L);

    }

    @Test
    void shouldReturnEmptyList_whenFindByProjectIdAndStatus_givenNotExistingProjectId() {
        // given / when
        var actual = taskRepository.findByProjectIdAndStatus(111L, null);
        // then
        assertThat(actual).isEmpty();
    }
}
