package by.teachmeskills.devteam.repository;

import by.teachmeskills.devteam.common.AbstractJpaTest;
import by.teachmeskills.devteam.entity.Project;
import by.teachmeskills.devteam.entity.Role;
import by.teachmeskills.devteam.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/testdata/jpa/userRepositoryTestData.sql")
class UserRepositoryJpaTest extends AbstractJpaTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnUserEntityWithCorrectlyMappedData_whenFindById_givenIdOfExistingUser() {
        // given
        var givenUserId = 4L;
        // when
        var actual = userRepository.findById(givenUserId);
        // then
        assertThat(actual).isPresent().get().satisfies(
                user -> {
                    assertThat(user.getId()).isEqualTo(givenUserId);
                    assertThat(user.getUsername()).isEqualTo("developer");
                    assertThat(user.isActive()).isTrue();
                    assertThat(user.getPassword()).isEqualTo("$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG");
                    assertThat(user.getRoles()).containsExactlyInAnyOrder(Role.USER, Role.DEVELOPER);
                    assertThat(user.getFirstName()).isEqualTo("Jakub");
                    assertThat(user.getLastName()).isEqualTo("Zieliński");
                    assertThat(user.getEmail()).isEqualTo("jakub.zielinski@example.com");
                    assertThat(user.getContacts()).isEqualTo("tel:+48 600 444 444");
                    assertThat(user.getPrice()).isEqualTo(120);
                    assertThat(user.getSkills()).isEqualTo("Java, Spring Boot");
                    assertThat(user.getProjects()).extracting(Project::getId).containsExactlyInAnyOrder(100L, 101L, 103L);
                }
        );
    }

    @Test
    void shouldReturnUser_whenFindByUsername_givenUsernameOfExistingUser() {
        // given
        var givenUsername = "manager";
        // when
        var actual = userRepository.findByUsername(givenUsername);
        // then
        assertThat(actual).isPresent();
        assertThat(actual.get().getUsername()).isEqualTo(givenUsername);
    }

    @Test
    void shouldReturnEmptyOptional_whenFindByUsername_givenUnknownUsername() {
        // given
        var givenUsername = "unknown";
        // when
        var actual = userRepository.findByUsername(givenUsername);
        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnListOfMatchedUsersInCorrectOrder_whenFindAllByRolesContainsOrderByUsernameAsc_givenDeveloperRole() {
        // given / when
        var actual = userRepository.findAllByRolesContainsOrderByUsernameAsc(Role.DEVELOPER);
        // then
        assertThat(actual).hasSize(3);
        assertThat(actual).extracting(User::getUsername).containsExactly("developer", "developer2", "developer3");
    }

}
