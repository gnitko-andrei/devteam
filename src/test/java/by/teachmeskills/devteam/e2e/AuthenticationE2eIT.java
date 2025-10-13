package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Scope: login/logout/session lifecycle.
 * Fixtures: @Sql user rows with BCrypt hashes (admin/customer/etc.).
 * Cases (3–4):
 * 	•	Bad credentials: POST /login → 200, login page with error.
 * 	•	Good credentials: POST /login → 302 to home (or your post-login page).
 * 	•	Logout: POST /logout (with CSRF) → 302 /login?logout, then /admin redirects to /login.
 */
@Sql(value = "/testdata/e2e/authenticationItTestData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AuthenticationE2eIT extends AbstractE2eTest {

    @Test
    void shouldAuthenticateUserAndRedirectToHome_whenPostLogin_givenExistingUserCredentials() {
        // given / when
        var actual = loginAs("testuser", "1");
        // then
        assertThat(actual.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(actual.getHeaders().getLocation()).isNotNull();
        assertThat(actual.getHeaders().getLocation().getPath()).isEqualTo("/");
    }

    @Test
    void shouldOpenUserProfile_whenGetUserProfile_givenAuthenticatedUser(){
        // given
        loginAs("testuser", "1");
        // when
        var actual = rest.getForEntity("/user", String.class);
        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        final var actualUserProfileHtml = Jsoup.parse(actual.getBody());
        assertThat(actualUserProfileHtml.title()).isEqualTo("User Profile");
        assertThat(actualUserProfileHtml.selectFirst("[data-testid=user-profile]")).isNotNull();
    }

    @Test
    void shouldRedirectToLoginWithErrorParam_whenPostLogin_givenUserWrongPassword() {
        // given / when
        var actual = loginAs("testuser", "wrong—password");
        // then
        assertThat(actual.getStatusCode().is3xxRedirection()).isTrue();
        var loc = actual.getHeaders().getLocation();
        assertThat(loc).isNotNull();
        assertThat(loc.getPath()).endsWith("/login");
        assertThat(loc.getQuery()).contains("error");
    }

    @Test
    void shouldRedirectToLoginWithLogoutParam_whenPostLogout_givenAuthenticatedUser() {
        // given
        loginAs("testuser", "1");
        // when
        var actual = postFormWithCsrf("/", "/logout", new LinkedMultiValueMap<>());
        // then
        assertThat(actual.getStatusCode().is3xxRedirection()).isTrue();
        var loc = actual.getHeaders().getLocation();
        assertThat(loc).isNotNull();
        assertThat(loc.getPath()).endsWith("/login");
        assertThat(loc.getQuery()).contains("logout");

        var userAfterLogout = rest.getForEntity("/user", String.class);
        assertThat(userAfterLogout.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(userAfterLogout.getHeaders().getLocation().getPath()).endsWith("/login");
    }
}
