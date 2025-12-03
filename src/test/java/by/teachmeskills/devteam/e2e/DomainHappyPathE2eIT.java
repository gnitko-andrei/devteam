package by.teachmeskills.devteam.e2e;

import by.teachmeskills.devteam.common.AbstractE2eTest;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Scope: tiniest, end-to-end business flow (touches DB, web, security, views).
 * Fixtures: base users/roles; optionally seed one customer/project if needed.
 * Cases (2–3):
 * 	•	CUSTOMER (or ADMIN) creates Project → capture Location/ID.
 * 	•	MANAGER creates Task under that project → capture ID.
 * 	•	DEVELOPER start work on the task → GET project tasks shows updated task status.
 * (Keep it deterministic: fixed names, parse IDs from Location header, or use known IDs from @Sql.)
 */
@Slf4j
@Sql(value = "/testdata/e2e/e2eCommonTestData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "/testdata/e2e/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class DomainHappyPathE2eIT extends AbstractE2eTest {

    public static final String NEW_PROJECT_NAME = "new_project";
    public static final String NEW_PROJECT_SPECIFICATION = "New project specification";
    public static final String MANAGER_4_NAME = "Maria Wiśniewska";
    public static final String PROJECT_STATUS_NEW = "NEW";
    public static final String CUSTOMER_USERNAME = "customer";
    public static final String CUSTOMER_PASSWORD = "1";
    public static final String TESTUSER_USERNAME = "testuser";
    public static final String TESTUSER_PASSWORD = "secret123";
    public static final String DEVELOPER_ROLE = "DEVELOPER";
    public static final String DEVELOPER_ROLE_NAME = "Developer";
    public static final String TESTUSER_FIRST_NAME = "Filip";
    public static final String TESTUSER_LAST_NAME = "Kaczmarek";
    public static final String TESTUSER_EMAIL = "filip.kaczmarek@example.com";
    public static final String TESTUSER_CONTACTS = "tel:+48 600 123 456";
    public static final String TESTUSER_SKILLS = "React, REST";
    public static final String MANAGER_USERNAME = "manager";
    public static final String MANAGER_PASSWORD = "1";
    public static final String NEW_TASK_NAME = "New Task";
    public static final String NEW_TASK_DESCRIPTION = "New Task Description";
    public static final String TASK_STATUS_NEW = "NEW";
    public static final String DEVELOPER_USERNAME = "developer";
    public static final String DEVELOPER_PASSWORD = "1";
    public static final String TASK_2000_NAME = "Gather requirements";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";

    @Test
    void shouldCreateNewUser_whenNewUserRegistration_givenRegistrationFormData() {
        // given
        MultiValueMap<String, String> givenForm = new LinkedMultiValueMap<>();
        givenForm.add("username", TESTUSER_USERNAME);
        givenForm.add("password", TESTUSER_PASSWORD);
        givenForm.add("userRole", DEVELOPER_ROLE);
        givenForm.add("firstName", TESTUSER_FIRST_NAME);
        givenForm.add("lastName", TESTUSER_LAST_NAME);
        givenForm.add("email", TESTUSER_EMAIL);
        givenForm.add("contacts", TESTUSER_CONTACTS);
        givenForm.add("skills", TESTUSER_SKILLS);
        // when / then
        var actualPost = postFormWithCsrf("/registration", "/registration", givenForm);
        assertRedirect(actualPost, "/login");
        var actualLogin = loginAs(TESTUSER_USERNAME, TESTUSER_PASSWORD);
        assertRedirect(actualLogin, "/");
        var actualGetUserProfile = rest.getForEntity("/user", String.class);
        assertHtmlPage(actualGetUserProfile, "user-profile");
        Document doc = Jsoup.parse(actualGetUserProfile.getBody());
        assertThat(doc.selectFirst("[data-testid=username]").text()).contains(TESTUSER_USERNAME);
        assertThat(doc.selectFirst("[data-testid=user-roles]").text()).isEqualTo(DEVELOPER_ROLE_NAME);
        assertThat(doc.selectFirst("[data-testid=user-firstname]").text()).isEqualTo(TESTUSER_FIRST_NAME);
        assertThat(doc.selectFirst("[data-testid=user-lastname]").text()).isEqualTo(TESTUSER_LAST_NAME);
        assertThat(doc.selectFirst("[data-testid=user-email]").text()).isEqualTo(TESTUSER_EMAIL);
        assertThat(doc.selectFirst("[data-testid=user-contacts]").text()).isEqualTo(TESTUSER_CONTACTS);
        assertThat(doc.selectFirst("[data-testid=user-skills]").text()).isEqualTo(TESTUSER_SKILLS);
    }

    @Test
    void shouldCreateNewProject_whenCustomerCreatNeProject_givenAuthenticatedCustomerAndNewProjectFormData() {
        // given
        loginAs(CUSTOMER_USERNAME, CUSTOMER_PASSWORD);
        MultiValueMap<String, String> givenForm = new LinkedMultiValueMap<>();
        givenForm.add("newProjectName", NEW_PROJECT_NAME);
        givenForm.add("newProjectSpecification", NEW_PROJECT_SPECIFICATION);
        givenForm.add("managerId", "4");
        // when / then
        var actualPost = postFormWithCsrf("/projects", "/projects", givenForm);
        assertRedirect(actualPost, "/projects");

        var actualGet = rest.getForEntity("/projects", String.class);
        assertHtmlPage(actualGet, "projects-list");
        Document doc = Jsoup.parse(actualGet.getBody());
        var card = doc.selectFirst(".card:has([data-testid=project-name]:matchesOwn(^new_project$))");
        assertThat(card)
                .as("New project card is missing")
                .isNotNull();
        assertThat(card.selectFirst("[data-testid=project-status]").text())
                .isEqualTo(PROJECT_STATUS_NEW);
        assertThat(card.selectFirst("[data-testid=manager-name]").text())
                .isEqualTo(MANAGER_4_NAME);
    }

    @Test
    void shouldCreateNewProjectTask_whenManagerCreateProjectTask_givenNewTaskFormData() {
        // given
        loginAs(MANAGER_USERNAME, MANAGER_PASSWORD);
        MultiValueMap<String, String> givenForm = new LinkedMultiValueMap<>();
        givenForm.add("name", NEW_TASK_NAME);
        givenForm.add("description", NEW_TASK_DESCRIPTION);
        // when / then
        var actualGetProjectTasks = rest.getForEntity("/projects/100/tasks", String.class);
        assertHtmlPage(actualGetProjectTasks, "tasks-list");
        var actualPost = postFormWithCsrf("/projects/100/tasks", "/projects/100/tasks", givenForm);
        assertRedirect(actualPost, "/projects/100/tasks");
        var actualGetProjectTasks2 = rest.getForEntity("/projects/100/tasks", String.class);
        assertHtmlPage(actualGetProjectTasks2, "tasks-list");
        Document doc = Jsoup.parse(actualGetProjectTasks2.getBody());
        var card = doc.selectFirst(String.format(".card:has([data-testid=task-name]:matchesOwn(^%s$))", NEW_TASK_NAME));
        assertThat(card)
                .as("New task card is missing")
                .isNotNull();
        assertThat(card.selectFirst("[data-testid=task-name]").text())
                .isEqualTo(NEW_TASK_NAME);
        assertThat(card.selectFirst("[data-testid=task-description]").text())
                .isEqualTo(NEW_TASK_DESCRIPTION);
        assertThat(card.selectFirst("[data-testid=task-status]").text())
                .isEqualTo(TASK_STATUS_NEW);
    }

    @Test
    void shouldSetStatusInProgress_whenUserStartTask_givenNewTask() {
        // given
        loginAs(DEVELOPER_USERNAME, DEVELOPER_PASSWORD);
        MultiValueMap<String, String> givenForm = new LinkedMultiValueMap<>();
        givenForm.add("status", STATUS_IN_PROGRESS);
        // when / then
        var actualGetTasks = rest.getForEntity("/projects/100/tasks", String.class);
        assertHtmlPage(actualGetTasks, "tasks-list");
        var actualPost = postFormWithCsrf("/projects/100/tasks", "/projects/100/tasks/2000", givenForm);
        assertRedirect(actualPost, "/projects/100/tasks");
        var actualGetTasks2 = rest.getForEntity("/projects/100/tasks", String.class);
        assertHtmlPage(actualGetTasks2, "tasks-list");
        Document doc = Jsoup.parse(actualGetTasks2.getBody());
        var card = doc.selectFirst(String.format(".card:has([data-testid=task-name]:matchesOwn(^%s$))", TASK_2000_NAME));
        assertThat(card.selectFirst("[data-testid=task-status]").text())
                .isEqualTo(STATUS_IN_PROGRESS);
    }
}
