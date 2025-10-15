package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Scope: login/logout/session lifecycle.
 * Fixtures: @Sql user rows with BCrypt hashes (admin/customer/etc.).
 * Cases (3–4):
 * 	•	Bad credentials: POST /login → 200, login page with error.
 * 	•	Good credentials: POST /login → 302 to home (or your post-login page).
 * 	•	Logout: POST /logout (with CSRF) → 302 /login?logout, then /admin redirects to /login.
 */
@Sql(value = "/testdata/e2e/e2eCommonTestData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "/testdata/e2e/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class AuthenticationE2eIT extends AbstractE2eTest {

    @Test
    void shouldAuthenticateUserAndRedirectToHome_whenPostLogin_givenExistingUserCredentials() {
        // given / when
        var actual = loginAs("user", "1");
        // then
        assertRedirect(actual, "/");
    }

    @Test
    void shouldOpenUserProfile_whenGetUserProfile_givenAuthenticatedUser(){
        // given
        loginAs("user", "1");
        // when
        var actual = rest.getForEntity("/user", String.class);
        // then
        assertHtmlPage(actual,  "user-profile");
    }

    @Test
    void shouldRedirectToLoginWithErrorParam_whenPostLogin_givenUserWrongPassword() {
        // given / when
        var actual = loginAs("user", "wrong—password");
        // then
        assertRedirect(actual, "/login", "error");
    }

    @Test
    void shouldRedirectToLoginWithLogoutParam_whenPostLogout_givenAuthenticatedUser() {
        // given
        loginAs("user", "1");
        // when / then
        var actual = postFormWithCsrf("/", "/logout", new LinkedMultiValueMap<>());
        assertRedirect(actual, "/login", "logout");

        var responseAfterLogout = rest.getForEntity("/user", String.class);
        assertRedirect(responseAfterLogout, "/login");
    }
}
