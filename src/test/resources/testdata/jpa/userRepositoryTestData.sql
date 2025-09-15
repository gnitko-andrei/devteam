INSERT INTO
	user (id, active, username, password, first_name, last_name, email, contacts, price, skills)
VALUES
  (1, TRUE, 'admin', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Anna', 'Kowalska', 'anna.kowalska@example.com', 'tel:+48 600 111 111', NULL, NULL),
  (2, TRUE, 'customer', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Piotr', 'Nowak', 'piotr.nowak@example.com', 'tel:+48 600 222 222', NULL, NULL),
  (3, TRUE, 'manager', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Maria', 'Wiśniewska', 'maria.wisniewska@example.com', 'tel:+48 600 333 333', NULL, NULL),
  (4, TRUE, 'developer', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Jakub', 'Zieliński', 'jakub.zielinski@example.com', 'tel:+48 600 444 444', 120, 'Java, Spring Boot'),
  (10, TRUE, 'developer3', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Filip', 'Kaczmarek', 'filip.kaczmarek@example.com', 'tel:+48 600 123 456', 110, 'React, REST'),
  (11, TRUE, 'developer2', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Agnieszka', 'Pawlak', 'agnieszka.pawlak@example.com', 'tel:+48 600 234 567', 130, 'SQL, Docker');

  -- Roles mapping
  INSERT INTO user_role (user_id, roles)
  VALUES
    (1, 'USER'),
    (1, 'ADMIN'),
    (2, 'USER'),
    (2, 'CUSTOMER'),
    (3, 'USER'),
    (3, 'MANAGER'),
    (4, 'USER'),
    (4, 'DEVELOPER'),
    (10, 'USER'),
    (10, 'DEVELOPER'),
    (11, 'USER'),
    (11, 'DEVELOPER');

INSERT INTO project (id, name, status, specification, customer_id, manager_id)
VALUES
  (100, 'Customer Portal', 'NEW', 'As a customer, I want to log in securely, update my personal data, and track all my orders in one place, so that I can manage my account without calling support.\n\nAcceptance criteria:\n- Login and registration forms with email verification\n- Profile management with contact info and preferences\n- Order history with filtering by date and status\n\nAdditional notes:\n- Must comply with GDPR\n- Needs support for multi-language UI', 2, 3),
  (101, 'Mobile App', 'PENDING_MANAGER', 'As a busy professional, I want a mobile app that works both offline and online, notifies me about task updates, and synchronizes automatically, so that I stay productive on the go.\n\nAcceptance criteria:\n- Offline caching of tasks and time logs\n- Push notifications for deadlines and assignments\n- Automatic sync with server when online\n\nAdditional notes:\n- Must run on both iOS and Android\n- Should follow platform UI guidelines', 2, 3),
  (102, 'Billing System', 'IN_PROGRESS', 'As a finance officer, I want the system to generate invoices with VAT, discounts, and currency conversion, and send them via email to clients, so that billing is automated and accurate.\n\nAcceptance criteria:\n- Automatic invoice generation on task completion\n- Support multiple currencies with real-time conversion\n- Email distribution of invoices to customers\n\nAdditional notes:\n- Integrate with external accounting system\n- Provide detailed audit logs for all transactions', 2, 3),
  (103, 'Reporting Module', 'COMPLETED', 'As a manager, I want dashboards and scheduled reports that summarize team performance and KPIs, so that I can make informed decisions quickly.\n\nAcceptance criteria:\n- Interactive charts with filters\n- Export to PDF and Excel\n- Weekly email reports\n\nAdditional notes:\n- Reports should be customizable by role\n- Data refresh should be near real-time', 2, 3);

INSERT INTO developer_project (project_id, developer_id)
VALUES
-- Customer Portal developers
  (100, 4),(100, 11),
-- Mobile App developers
  (101, 4),(101, 10),
-- Reporting Module developers
  (103, 4);
