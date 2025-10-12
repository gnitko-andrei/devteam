package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.jpa.AbstractE2eTest;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationHttpSmokeIT extends AbstractE2eTest {

    @Test
    void shouldReturnCompleteHomePage_whenGetRoot_givenNoSpecificPreconditions() {
        // given / when
        var actual = rest.getForEntity("/", String.class);
        final var actualHtml = Jsoup.parse(actual.getBody());
        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualHtml.title()).isEqualTo("DevTeam");
        assertThat(actualHtml.selectFirst("[data-testid=home-page]")).isNotNull();
        assertThat(actualHtml.selectFirst("[data-testid=page-header]")).isNotNull();
        assertThat(actualHtml.selectFirst("[data-testid=page-footer]")).isNotNull();
        assertThat(actualHtml.outerHtml()).contains("data-testid: headerfiles");
        assertThat(actualHtml.outerHtml()).contains("data-testid: page-imports");
    }

    @Test
    void shouldReturnLoginPage_whenGetLogin_givenNoSpecificPreconditions() {
        // given / when
        var actual = rest.getForEntity("/login", String.class);
        final var actualHtml = Jsoup.parse(actual.getBody());
        // then
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualHtml.title()).isEqualTo("Login");
        assertThat(actualHtml.selectFirst("[data-testid=login-form]")).isNotNull();
    }
}
