INSERT INTO user (id, active, username, password, first_name, last_name, email, contacts, price, skills)
  VALUES
    (2, TRUE, 'customer2', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Jan', 'Krawczyk', 'jan.krawczyk@example.com', 'tel:+48 600 666 666', NULL, NULL),
    (3, TRUE, 'manager', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Maria', 'Wiśniewska', 'maria.wisniewska@example.com', 'tel:+48 600 333 333', NULL, NULL);

INSERT INTO project (id, name, status, specification, customer_id, manager_id)
  VALUES
    (100, 'Customer Portal', 'NEW', 'As a customer, I want to log in securely, update my personal data, and track all my orders in one place, so that I can manage my account without calling support.\n\nAcceptance criteria:\n- Login and registration forms with email verification\n- Profile management with contact info and preferences\n- Order history with filtering by date and status\n\nAdditional notes:\n- Must comply with GDPR\n- Needs support for multi-language UI', 2, 3),
    (101, 'Mobile App', 'PENDING_MANAGER', 'As a busy professional, I want a mobile app that works both offline and online, notifies me about task updates, and synchronizes automatically, so that I stay productive on the go.\n\nAcceptance criteria:\n- Offline caching of tasks and time logs\n- Push notifications for deadlines and assignments\n- Automatic sync with server when online\n\nAdditional notes:\n- Must run on both iOS and Android\n- Should follow platform UI guidelines', 2, 3);


INSERT INTO task (id, name, status, description, time, price, project_id)
  VALUES
-- Project 100
    (2000, 'Gather requirements', 'NEW', 'As a business analyst, I need to conduct interviews, collect user stories, and document them in Confluence so that the team understands the scope.\n\nAcceptance criteria:\n- At least 5 stakeholder interviews completed\n- User stories documented and approved by customer\n\nAdditional notes:\n- Use Miro for workflow diagrams', 5, 520, 100),
    (2001, 'Design UI mockups', 'NEW', 'As a designer, I want to create Figma mockups with responsive breakpoints and accessibility guidelines so that developers have a clear visual reference.\n\nAcceptance criteria:\n- Desktop and mobile versions\n- Color contrast compliant with WCAG 2.1\n\nAdditional notes:\n- Include dark mode variant', 6, 640, 100),
    (2002, 'Setup DB schema', 'IN_PROGRESS', 'As a backend developer, I want normalized DB tables with Flyway migrations so that the application has a stable data foundation.\n\nAcceptance criteria:\n- User, project, and task tables created\n- Constraints and indexes applied\n\nAdditional notes:\n- Add seed data for local testing', 7, 730, 100),
    (2003, 'Implement login', 'IN_PROGRESS', 'As a user, I want to log in securely with email and password so that I can access the portal safely.\n\nAcceptance criteria:\n- Passwords hashed with BCrypt\n- Error messages for failed logins\n\nAdditional notes:\n- Account lockout after 5 failed attempts', 8, 870, 100),
    (2004, 'Deploy prototype', 'DONE', 'As a DevOps engineer, I want to deploy the prototype to staging with Docker Compose so that testers can try features early.\n\nAcceptance criteria:\n- Accessible at staging URL\n- Basic smoke tests pass\n\nAdditional notes:\n- Prepare rollback instructions', 4, 450, 100),
    (2005, 'Accessibility audit', 'NEW', 'As a QA engineer, I want to test for WCAG compliance so that the app is usable by everyone.\n\nAcceptance criteria:\n- Screen reader navigation works\n- Color contrast verified\n\nAdditional notes:\n- Include keyboard navigation tests', 4, 400, 100),
    (2006, 'Error logging', 'NEW', 'As a developer, I want centralized logging with SLF4J and ELK so that errors are traceable.\n\nAcceptance criteria:\n- Errors logged with stacktrace\n- Logs visible in Kibana\n\nAdditional notes:\n- Mask sensitive fields', 600, 6, 100),
-- Project 101
    (2010, 'API design', 'NEW', 'As a developer, I want OpenAPI docs for all endpoints so that teams can integrate in parallel.\n\nAcceptance criteria:\n- Endpoints documented\n- Examples provided\n\nAdditional notes:\n- Review by FE/BE leads', 5, 540, 101),
    (2011, 'Authentication', 'IN_PROGRESS', 'As a user, I want secure token-based authentication with JWT so that I stay logged in safely.\n\nAcceptance criteria:\n- Token refresh implemented\n- Expired tokens rejected\n\nAdditional notes:\n- Store tokens securely client-side', 7, 710, 101),
    (2012, 'Push notifications', 'NEW', 'As a mobile user, I want push alerts for deadlines so that I never miss important updates.\n\nAcceptance criteria:\n- Notifications on Android & iOS\n- Delivered within 5 seconds\n\nAdditional notes:\n- Support deep links', 9, 920, 101),
    (2013, 'Offline mode', 'NEW', 'As a user, I want offline caching and auto-sync so that I can work without internet.\n\nAcceptance criteria:\n- Tasks saved locally\n- Auto-sync on reconnect\n\nAdditional notes:\n- Handle conflicts gracefully', 6, 650, 101),
    (2014, 'App store release', 'IN_PROGRESS', 'As a release manager, I want builds and assets prepared for App Store and Google Play so that the app can be published.\n\nAcceptance criteria:\n- Metadata filled\n- TestFlight build uploaded\n\nAdditional notes:\n- Comply with store guidelines', 10, 1020, 101),
    (2015, 'UI testing', 'NEW', 'As QA, I want automated UI tests with Selenium so that regressions are caught.\n\nAcceptance criteria:\n- Login and task screens covered\n- Tests pass in CI/CD\n\nAdditional notes:\n- Integrate with GitHub Actions', 8, 800, 101);
