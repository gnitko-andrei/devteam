package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

/**
 * Scope: custom error views render correctly.
 * Pre-req: none (or login for 403 scenario).
 * Cases (2):
 * 	•	GET an unknown path → 404 + your 404 template marker (e.g., [data-testid=page-404]).
 * 	•	Logged-in non-admin → GET /admin → 403 + your 403 template marker.
 */
@Sql(value = "/testdata/e2e/e2eCommonTestData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "/testdata/e2e/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class ErrorPagesE2eIT extends AbstractE2eTest {

    @Test
    void shouldOpen404Page_whenGetUnknownPath_givenUnknownPath() {
        // given
        loginAs("testuser", "1");

        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.TEXT_HTML)); // force HTML
        var entity = new HttpEntity<Void>(headers);
        // when
        var actual = rest.exchange("/unknown-path", HttpMethod.GET, entity, String.class);
        // then
        assertHtmlPage(actual, "page-not-found", HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldOpenErrorPage_whenGetAdminPath_givenNoAdminUserAuthenticated() {
        // given
        loginAs("testuser", "1");

        var headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.TEXT_HTML)); // force HTML
        var entity = new HttpEntity<Void>(headers);
        // when
        var actual = rest.exchange("/admin", HttpMethod.GET, entity, String.class);
        // then
        assertHtmlPage(actual, "error-page", HttpStatus.FORBIDDEN);
    }
}
