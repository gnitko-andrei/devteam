package by.teachmeskills.devteam.common;

import by.teachmeskills.devteam.config.TestHttpClientConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
}
