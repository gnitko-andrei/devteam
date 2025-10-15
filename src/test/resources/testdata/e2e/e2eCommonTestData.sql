INSERT INTO
	user (id, active, username, password, first_name, last_name, email, contacts, price, skills)
VALUES
  (1, TRUE, 'user', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Anna', 'Kowalska', 'anna.kowalska@example.com', 'tel:+48 600 111 111', NULL, NULL),
  (2, TRUE, 'admin', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Anna', 'Kowalska', 'anna.kowalska@example.com', 'tel:+48 600 111 111', NULL, NULL),
  (3, TRUE, 'customer', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Piotr', 'Nowak', 'piotr.nowak@example.com', 'tel:+48 600 222 222', NULL, NULL),
  (4, TRUE, 'manager', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Maria', 'Wiśniewska', 'maria.wisniewska@example.com', 'tel:+48 600 333 333', NULL, NULL),
  (5, TRUE, 'developer', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Jakub', 'Zieliński', 'jakub.zielinski@example.com', 'tel:+48 600 444 444', 120, 'Java, Spring Boot');

  -- Roles mapping
  INSERT INTO user_role (user_id, roles)
  VALUES
    (1, 'USER'),
    (2, 'USER'),
    (2, 'ADMIN'),
    (3, 'USER'),
    (3, 'CUSTOMER'),
    (4, 'USER'),
    (4, 'MANAGER'),
    (5, 'USER'),
    (5, 'DEVELOPER');

INSERT INTO
    project (id, name, status, specification, customer_id, manager_id)
VALUES
  (100, 'Customer Portal', 'NEW', 'As a customer, I want to log in securely, update my personal data, and track all my orders in one place, so that I can manage my account without calling support.\n\nAcceptance criteria:\n- Login and registration forms with email verification\n- Profile management with contact info and preferences\n- Order history with filtering by date and status\n\nAdditional notes:\n- Must comply with GDPR\n- Needs support for multi-language UI', 3, 4);

INSERT INTO task (id, name, status, description, time, price, project_id)
VALUES
  (2000, 'Gather requirements', 'NEW', 'As a business analyst, I need to conduct interviews, collect user stories, and document them in Confluence so that the team understands the scope.\n\nAcceptance criteria:\n- At least 5 stakeholder interviews completed\n- User stories documented and approved by customer\n\nAdditional notes:\n- Use Miro for workflow diagrams', 0, 0, 100);

UPDATE hibernate_sequence
SET next_val = 13000;