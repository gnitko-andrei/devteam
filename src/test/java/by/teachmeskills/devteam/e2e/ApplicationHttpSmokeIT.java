package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("smoke")
class ApplicationHttpSmokeIT extends AbstractE2eTest {

    @Test
    void shouldReturnCompleteHomePage_whenGetRoot_givenNoSpecificPreconditions() {
        // given / when
        var actual = rest.getForEntity("/", String.class);
        final var actualHtml = Jsoup.parse(actual.getBody());
        // then
        assertHtmlPage(actual, "home-page");
        // additional asserts for common fragments
        assertThat(actualHtml.selectFirst("[data-testid=page-header]")).isNotNull();
        assertThat(actualHtml.selectFirst("[data-testid=page-footer]")).isNotNull();
        assertThat(actualHtml.outerHtml()).contains("data-testid: headerfiles");
        assertThat(actualHtml.outerHtml()).contains("data-testid: page-imports");
    }

    @Test
    void shouldReturnLoginPage_whenGetLogin_givenNoSpecificPreconditions() {
        // given / when
        var actual = rest.getForEntity("/login", String.class);
        // then
        assertHtmlPage(actual, "login-form");
    }
}
