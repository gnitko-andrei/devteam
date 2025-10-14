package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Scope: CSRF enforcement on state-changing endpoints.
 * Fixtures: minimal user capable of accessing the form (often ADMIN or MANAGER).
 * Cases (2–3):
 * 	•	POST without _csrf to a protected action (e.g., /projects) → 403.
 * 	•	Same POST with _csrf (fetch token from GET form page) → 302/200 success.
 * 	•	(Optional) Verify token present in rendered form (hidden input exists).
 */
class CsrfE2eIT extends AbstractE2eTest {

    @Test
    void shouldReturn403_whenPostRegistration_givenNoCsrf() {
        // given
        var givenForm = new LinkedMultiValueMap<String, String>();
        givenForm.add("username", "testuser");
        givenForm.add("password", "1");
        givenForm.add("userRole", "DEVELOPER");

        var givenHttpHeaders = new HttpHeaders();
        givenHttpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // when
        var actual = rest.postForEntity("/registration", new HttpEntity<>(givenForm, givenHttpHeaders), String.class);
        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldRedirectAfterSuccessfulRegistration_whenPostRegistration_givenCsrf() {
        // given
        var givenForm = new LinkedMultiValueMap<String, String>();
        givenForm.add("username", "testuser1");
        givenForm.add("password", "1");
        givenForm.add("userRole", "DEVELOPER");
        // when
        var actual = postFormWithCsrf("/registration", "/registration", givenForm);
        // then
        assertThat(actual.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(actual.getHeaders().getLocation()).isNotNull();
        assertThat(actual.getHeaders().getLocation().getPath()).isEqualTo("/login");
    }
}
