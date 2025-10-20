package by.teachmeskills.devteam.repository;

import by.teachmeskills.devteam.common.AbstractJpaTest;
import by.teachmeskills.devteam.dto.project.ProjectFiltersDto;
import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.attributes.project.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/testdata/jpa/projectRepositoryTestData.sql")
class ProjectRepositoryJpaTest extends AbstractJpaTest {

    public static final long NO_ROLE_USER_ID = 0L;
    public static final long MANAGER_USER_ID = 3L;
    public static final long CUSTOMER_USER_ID = 1L;
    public static final long DEVELOPER_USER_ID = 5L;
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void shouldReturnProjectEntityWithCorrectlyMappedData_when_given() {
        // given
        var givenProjectId = 100L;
        var expectedSpecification = """
                As a customer, I want to log in securely, update my personal data, and track all my orders in one place, so that I can manage my account without calling support.
                
                Acceptance criteria:
                - Login and registration forms with email verification
                - Profile management with contact info and preferences
                - Order history with filtering by date and status
                
                Additional notes:
                - Must comply with GDPR
                - Needs support for multi-language UI""";
        // when
        var actual = projectRepository.findById(givenProjectId);
        // then
        assertThat(actual).isPresent().get().satisfies(project -> {
            assertThat(project.getId()).isEqualTo(givenProjectId);
            assertThat(project.getName()).isEqualTo("Customer Portal");
            assertThat(project.getSpecification()).isEqualTo(expectedSpecification);
            assertThat(project.getCustomer().getId()).isEqualTo(1L);
            assertThat(project.getManager().getId()).isEqualTo(MANAGER_USER_ID);
        });
    }

    @Test
    void shouldReturnProject_whenFindByName_givenExistingProjectName() {
        // given
        var givenProjectName = "Customer Portal";
        // when
        var actual = projectRepository.findByName(givenProjectName);
        // then
        assertThat(actual).isPresent().get()
                .satisfies(project -> assertThat(project.getName()).isEqualTo(givenProjectName));
    }

    @Test
    void shouldReturnEmptyOptional_whenFindByName_givenUnknownProjectName() {
        // given
        var givenProjectName = "unknown";
        // when
        var actual = projectRepository.findByName(givenProjectName);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnAllProjectsOfManager_whenFindManagerProjects_givenManagerId() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(MANAGER_USER_ID)
                .build();
        // when
        var actual = projectRepository.findManagerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(3).extracting(Project::getId)
                .containsExactlyInAnyOrder(100L, 101L, 104L);
    }


    @Test
    void shouldReturnFilteredByStatusProjectsOfManager_whenFindManagerProjects_givenManagerIdAndStatusFilter() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(MANAGER_USER_ID)
                .statusFilter(ProjectStatus.NEW)
                .build();
        // when
        var actual = projectRepository.findManagerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(2).extracting(Project::getId)
                .containsExactlyInAnyOrder(100L, 104L);
    }

    @Test
    void shouldReturnFilteredByNameProjectsOfManager_whenFindManagerProjects_givenManagerIdAndNameFilter() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(MANAGER_USER_ID)
                .nameFilter("  APP      ")
                .build();
        // when
        var actual = projectRepository.findManagerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(1).extracting(Project::getId)
                .containsExactlyInAnyOrder(101L);
    }

    @Test
    void shouldReturnFilteredProjectsOfManager_whenFindManagerProjects_givenFilterCombinationMatchingExistingProject() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(MANAGER_USER_ID)
                .statusFilter(ProjectStatus.PENDING_MANAGER)
                .nameFilter("  APP      ")
                .build();
        // when
        var actual = projectRepository.findManagerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(1).extracting(Project::getId)
                .containsExactlyInAnyOrder(101L);
    }


    @Test
    void shouldReturnFilteredProjectsOfManager_whenFindManagerProjects_givenFilterCombinationNotMatchAnything() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(MANAGER_USER_ID)
                .statusFilter(ProjectStatus.NEW)
                .nameFilter("  APP      ")
                .build();
        // when
        var actual = projectRepository.findManagerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnEmptyList_whenFindManagerProjects_givenNotManagerUserId() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(NO_ROLE_USER_ID)
                .build();
        // when
        var actual = projectRepository.findManagerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnAllProjectsOfCustomer_whenFindCustomerProjects_givenCustomerId() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(CUSTOMER_USER_ID)
                .build();
        // when
        var actual = projectRepository.findCustomerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(4).extracting(Project::getId)
                .containsExactlyInAnyOrder(100L, 103L, 104L, 106L);
    }

    @Test
    void shouldReturnFilteredByStatusProjectsOfCustomer_whenFindCustomerProjects_givenCustomerIdAndStatusFilter() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(CUSTOMER_USER_ID)
                .statusFilter(ProjectStatus.NEW)
                .build();
        // when
        var actual = projectRepository.findCustomerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(2).extracting(Project::getId)
                .containsExactlyInAnyOrder(100L, 104L);
    }

    @Test
    void shouldReturnFilteredByNameProjectsOfCustomer_whenFindCustomerProjects_givenCustomerIdAndNameFilter() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(CUSTOMER_USER_ID)
                .nameFilter("  SysTEM      ")
                .build();
        // when
        var actual = projectRepository.findCustomerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(1).extracting(Project::getId)
                .containsExactlyInAnyOrder(106L);
    }

    @Test
    void shouldReturnFilteredProjectsOfCustomer_whenFindCustomerProjects_givenFilterCombinationMatchingExistingProject() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(CUSTOMER_USER_ID)
                .statusFilter(ProjectStatus.NEW)
                .nameFilter("  Platform      ")
                .build();
        // when
        var actual = projectRepository.findCustomerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(1).extracting(Project::getId)
                .containsExactlyInAnyOrder(104L);
    }

    @Test
    void shouldReturnFilteredProjectsOfCustomer_whenFindCustomerProjects_givenFilterCombinationNotMatchAnything() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(CUSTOMER_USER_ID)
                .statusFilter(ProjectStatus.NEW)
                .nameFilter("  SysTEM      ")
                .build();
        // when
        var actual = projectRepository.findCustomerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnEmptyList_whenFindCustomerProjects_givenNotCustomerUserId() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(NO_ROLE_USER_ID)
                .build();
        // when
        var actual = projectRepository.findCustomerProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnAllProjectsOfDeveloper_whenFindDeveloperProjects_givenDeveloperId() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(DEVELOPER_USER_ID)
                .build();
        // when
        var actual = projectRepository.findDeveloperProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(5).extracting(Project::getId)
                .containsExactlyInAnyOrder(100L, 101L, 103L, 104L, 106L);
    }

    @Test
    void shouldReturnFilteredByStatusProjectsOfDeveloper_whenFindDeveloperProjects_givenDeveloperIdAndStatusFilter() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(DEVELOPER_USER_ID)
                .statusFilter(ProjectStatus.NEW)
                .build();
        // when
        var actual = projectRepository.findDeveloperProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(2).extracting(Project::getId)
                .containsExactlyInAnyOrder(100L, 104L);
    }

    @Test
    void shouldReturnFilteredByNameProjectsOfDeveloper_whenFindDeveloperProjects_givenDeveloperIdAndNameFilter() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(DEVELOPER_USER_ID)
                .nameFilter("  E-commerCE      ")
                .build();
        // when
        var actual = projectRepository.findDeveloperProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(1).extracting(Project::getId)
                .containsExactlyInAnyOrder(104L);
    }

    @Test
    void shouldReturnFilteredProjectsOfDeveloper_whenFindDeveloperProjects_givenFilterCombinationMatchingExistingProject() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(DEVELOPER_USER_ID)
                .statusFilter(ProjectStatus.NEW)
                .nameFilter("  Platform      ")
                .build();
        // when
        var actual = projectRepository.findDeveloperProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).hasSize(1).extracting(Project::getId)
                .containsExactlyInAnyOrder(104L);
    }

    @Test
    void shouldReturnFilteredProjectsOfDeveloper_whenFindDeveloperProjects_givenFilterCombinationNotMatchAnything() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(DEVELOPER_USER_ID)
                .statusFilter(ProjectStatus.NEW)
                .nameFilter("  Module      ")
                .build();
        // when
        var actual = projectRepository.findDeveloperProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnEmptyList_whenFindDeveloperProjects_givenNotDeveloperUserId() {
        // given
        var givenProjectFiltersDto = ProjectFiltersDto.builder()
                .userId(NO_ROLE_USER_ID)
                .build();
        // when
        var actual = projectRepository.findDeveloperProjects(givenProjectFiltersDto);
        // then
        assertThat(actual).isEmpty();
    }
}
