package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;
import org.jsoup.Jsoup;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Scope: role-based access control + anonymous redirects.
 * Fixtures: users for roles: ADMIN, MANAGER, DEVELOPER, CUSTOMER.
 * Cases (5–6 total, parametrize if you like):
 * 	•	Anonymous → /admin (and another protected page like /projects) → 302 to /login.
 * 	•	ADMIN → /admin → 200.
 * 	•	MANAGER/DEVELOPER/CUSTOMER → /admin → 403.
 * 	•	(Optional) Role-allowed endpoints: e.g., MANAGER → /projects → 200; CUSTOMER → /projects (view) → 200/403 per your rules.
 */
@Sql(value = "/testdata/e2e/e2eCommonTestData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "/testdata/e2e/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class RoleBasedAccessE2eIT extends AbstractE2eTest {

    static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of("ANONYMOUS", null, null, "/", HttpStatus.OK, "home-page", null),
                Arguments.of("ANONYMOUS", null, null, "/user", HttpStatus.FOUND, null, "/login"),
                Arguments.of("USER", "testuser", "1", "/user", HttpStatus.OK, "user-profile", null),
                Arguments.of("USER", "testuser", "1", "/admin", HttpStatus.FORBIDDEN, null, null),
                Arguments.of("ADMIN", "admin", "1", "/admin", HttpStatus.OK, "admin-panel", null),
                Arguments.of("ADMIN", "admin", "1", "/projects", HttpStatus.FORBIDDEN, null, null),
                Arguments.of("MANAGER", "manager", "1", "/projects", HttpStatus.OK, "projects-list", null),
                Arguments.of("MANAGER", "manager", "1", "/admin", HttpStatus.FORBIDDEN, null, null),
                Arguments.of("DEVELOPER", "developer", "1", "/projects", HttpStatus.OK, "projects-list", null),
                Arguments.of("DEVELOPER", "developer", "1", "/admin", HttpStatus.FORBIDDEN, null, null),
                Arguments.of("CUSTOMER", "customer", "1", "/projects", HttpStatus.OK, "projects-list", null),
                Arguments.of("CUSTOMER", "customer", "1", "/admin", HttpStatus.FORBIDDEN, null, null)
        );
    }

    @ParameterizedTest(name = "[{index}] {0} GET {3} -> {4}")
    @MethodSource("params")
    void should_when_given(String roleName, String username, String password, String path, HttpStatus expectedHttpStatusCode, String expectedPageTestId, String redirectLocation) {
        // given
        if(!"ANONYMOUS".equals(roleName)) {
            loginAs(username, password);
        }
        // when
        var actual = rest.getForEntity(path, String.class);
        // then
        assertThat(actual.getStatusCode()).isEqualTo(expectedHttpStatusCode);
        if(expectedPageTestId != null && !expectedPageTestId.isBlank()) {
            var actualHtml = Jsoup.parse(actual.getBody());
            assertThat(actualHtml.selectFirst(String.format("[data-testid=%s]", expectedPageTestId))).isNotNull();
        }
        if (redirectLocation != null && !redirectLocation.isBlank()) {
            assertRedirect(actual, redirectLocation);
        }
    }
}
