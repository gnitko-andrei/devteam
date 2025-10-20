package by.teachmeskills.devteam.common;

import by.teachmeskills.devteam.config.TestHttpClientConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.test.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.CookieManager;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("e2e")
@ActiveProfiles("test")
@Import(TestHttpClientConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractE2eTest extends MySqlContainerSupport {

    @Autowired
    protected TestRestTemplate rest;
    @Autowired
    protected CookieManager cookieManager;

    @BeforeEach
    void resetClientState() {
        cookieManager.getCookieStore().removeAll(); // forget session between tests
    }

    @BeforeEach
    void printConn() {
        System.out.printf("🐳 mysql host=localhost port=%d db=%s user=%s password=%s%n",
                mysql.getMappedPort(3306), mysql.getDatabaseName(), mysql.getUsername(), mysql.getPassword());
        String url = "jdbc:mysql://localhost:" + mysql.getMappedPort(3306) + "/" + mysql.getDatabaseName();
        System.out.println("🐳 jdbc url:            " + url);
        System.out.println("🐳 jdbc url (no-ssl):   " + url + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC\n");
    }

    protected String csrfFrom(String html) {
        Document doc = Jsoup.parse(html);
        Element token = doc.selectFirst("input[name=_csrf]");
        if (token == null) {
            throw new IllegalStateException("CSRF token not found on page");
        }
        String value = token.attr("value");
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("CSRF token input present but value is empty");
        }
        return value;
    }

    protected ResponseEntity<String> postFormWithCsrf(String getPagePath, String postPath, MultiValueMap<String, String> form) {
        // 1) GET page to obtain CSRF and session cookie
        ResponseEntity<String> page = rest.getForEntity(getPagePath, String.class);
        String csrf = csrfFrom(page.getBody());

        // 2) POST form with credentials + CSRF using the same session cookie
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(form);
        body.add("_csrf", csrf);

        return rest.postForEntity(postPath, new HttpEntity<>(body, httpHeaders), String.class);
    }

    protected ResponseEntity<String> loginAs(String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", username);
        form.add("password", password);
        return postFormWithCsrf("/login", "/login", form);
    }

    protected static void assertRedirect(ResponseEntity<String> actualResponse, String expectedRedirectPath, String... expectedQueryParams) {
        assertThat(actualResponse.getStatusCode().is3xxRedirection())
                .as("Expected 3XX REDIRECT but was %s", actualResponse.getStatusCode())
                .isTrue();
        final var location = actualResponse.getHeaders().getLocation();
        assertThat(location)
                .as("Missing Location header")
                .isNotNull();
        final var actualRedirectPath = location.getPath();
        assertThat(actualRedirectPath)
                .as("Expected redirect path to end with '%s' but was '%s'", expectedRedirectPath, actualRedirectPath)
                .endsWith(expectedRedirectPath);
        for (String param : expectedQueryParams) {
            final var actualLocationQuery = location.getQuery();
            assertThat(actualLocationQuery)
                    .as("Expected query to contain param '%s' but was: %s", param, actualLocationQuery)
                    .contains(param);
        }
    }

    protected static void assertHtmlPage(ResponseEntity<String> actualResponse, String dataTestId) {
        assertHtmlPage(actualResponse, dataTestId, HttpStatus.OK);
    }

    protected static void assertHtmlPage(ResponseEntity<String> actualResponse, String dataTestid, HttpStatus expectedStatus) {
        var location = actualResponse.getHeaders().getLocation();
        assertThat(actualResponse.getStatusCode())
                .as("Expected 200 OK but was %s (Location=%s)", actualResponse.getStatusCode(), location)
                .isEqualTo(expectedStatus);

        var contentType = actualResponse.getHeaders().getContentType();
        assertThat(contentType)
                .as("Content-Type header should be present")
                .isNotNull();
        assertThat(contentType.isCompatibleWith(MediaType.TEXT_HTML))
                .as("Expected Content-Type text/html but was %s", contentType)
                .isTrue();

        var body = actualResponse.getBody();
        assertThat(body)
                .as("Response body should not be empty")
                .isNotBlank();

        Document doc = Jsoup.parse(body);
        var selector = String.format("[data-testid=\"%s\"]", dataTestid);
        assertThat(doc.selectFirst(selector))
                .as("Missing page marker %s", selector)
                .isNotNull();
    }
}
