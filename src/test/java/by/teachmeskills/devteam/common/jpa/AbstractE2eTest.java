package by.teachmeskills.devteam.common.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.test.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractE2eTest extends MySqlContainerSupport {

    @Autowired
    protected TestRestTemplate rest;

    private static final Pattern CSRF = Pattern.compile("name=\"_csrf\"\\s+value=\"([^\"]+)\"");

    protected String csrfFrom(String html) {
        Matcher m = CSRF.matcher(html);
        if(!m.find()) {
            throw new IllegalStateException("CSRF token not found on page");
        } else {
            return m.group(1);
        }
    }

    protected ResponseEntity<String> postFormWithCsrf(String getPagePath, String postPath, MultiValueMap<String, String> form) {
        // 1) GET /login to obtain CSRF and session cookie
        ResponseEntity<String> page = rest.getForEntity(getPagePath, String.class);
        String csrf = csrfFrom(page.getBody());

        // 2) POST form with credentials + CSRF (TestRestTemplate keeps cookies)
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        form.add("_csrf", csrf);

        return rest.postForEntity(postPath, new HttpEntity<>(form, httpHeaders), String.class);
    }

    protected ResponseEntity<String> loginAs(String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", username);
        form.add("password", password);
        return postFormWithCsrf("/login", "/login", form);
    }
}
