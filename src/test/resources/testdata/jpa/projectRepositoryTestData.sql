INSERT INTO user (id, active, username, password, first_name, last_name, email, contacts, price, skills)
  VALUES
    (0, TRUE, 'user', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Aleksandra', 'Mazur', 'aleksandra.mazur@example.com', 'tel:+48 600 555 555', NULL, NULL),
    (1, TRUE, 'customer', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Piotr', 'Nowak', 'piotr.nowak@example.com', 'tel:+48 600 222 222', NULL, NULL),
    (2, TRUE, 'customer2', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Jan', 'Krawczyk', 'jan.krawczyk@example.com', 'tel:+48 600 666 666', NULL, NULL),
    (3, TRUE, 'manager', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Maria', 'Wiśniewska', 'maria.wisniewska@example.com', 'tel:+48 600 333 333', NULL, NULL),
    (4, TRUE, 'manager2', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Tomasz', 'Wójcik', 'tomasz.wojcik@example.com', 'tel:+48 600 888 888', NULL, NULL),
    (5, TRUE, 'developer', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Jakub', 'Zieliński', 'jakub.zielinski@example.com', 'tel:+48 600 444 444', 120, 'Java, Spring Boot'),
    (6, TRUE, 'developer2', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Filip', 'Kaczmarek', 'filip.kaczmarek@example.com', 'tel:+48 600 123 456', 110, 'React, REST');


INSERT INTO project (id, name, status, specification, customer_id, manager_id)
VALUES
  (100, 'Customer Portal', 'NEW', 'As a customer, I want to log in securely, update my personal data, and track all my orders in one place, so that I can manage my account without calling support.\n\nAcceptance criteria:\n- Login and registration forms with email verification\n- Profile management with contact info and preferences\n- Order history with filtering by date and status\n\nAdditional notes:\n- Must comply with GDPR\n- Needs support for multi-language UI', 1, 3),
  (101, 'Mobile App', 'PENDING_MANAGER', 'As a busy professional, I want a mobile app that works both offline and online, notifies me about task updates, and synchronizes automatically, so that I stay productive on the go.\n\nAcceptance criteria:\n- Offline caching of tasks and time logs\n- Push notifications for deadlines and assignments\n- Automatic sync with server when online\n\nAdditional notes:\n- Must run on both iOS and Android\n- Should follow platform UI guidelines', 2, 3),
  (102, 'Billing System', 'IN_PROGRESS', 'As a finance officer, I want the system to generate invoices with VAT, discounts, and currency conversion, and send them via email to clients, so that billing is automated and accurate.\n\nAcceptance criteria:\n- Automatic invoice generation on task completion\n- Support multiple currencies with real-time conversion\n- Email distribution of invoices to customers\n\nAdditional notes:\n- Integrate with external accounting system\n- Provide detailed audit logs for all transactions', 2, 4),
  (103, 'Reporting Module', 'COMPLETED', 'As a manager, I want dashboards and scheduled reports that summarize team performance and KPIs, so that I can make informed decisions quickly.\n\nAcceptance criteria:\n- Interactive charts with filters\n- Export to PDF and Excel\n- Weekly email reports\n\nAdditional notes:\n- Reports should be customizable by role\n- Data refresh should be near real-time', 1, 4),
  (104, 'E-commerce Platform', 'NEW', 'As a shopper, I want to search products, read reviews, and checkout securely, so that I can purchase confidently.\n\nAcceptance criteria:\n- Product catalog with categories and filters\n- Shopping cart and wishlist\n- Payment via card and PayPal\n\nAdditional notes:\n- Support discount codes\n- Track stock availability and backorders', 1, 3),
  (105, 'CRM Tool', 'IN_PROGRESS', 'As a sales team, we want a CRM to track leads and automate follow-ups, so that we can close deals faster.\n\nAcceptance criteria:\n- Lead pipeline board\n- Automated reminders and notifications\n- Email integration\n\nAdditional notes:\n- Provide role-based permissions\n- Support exporting leads to CSV', 2, 4),
  (106, 'HR System', 'PENDING_MANAGER', 'As an HR manager, I want to manage employee data and payroll, so that HR processes are streamlined.\n\nAcceptance criteria:\n- Employee profiles with personal and job info\n- Payroll calculation with deductions and bonuses\n- Leave request and approval workflow\n\nAdditional notes:\n- Include performance review history\n- Integrate with external payroll services', 1, 4);

INSERT INTO developer_project (project_id, developer_id)
VALUES
-- Customer Portal developers
  (100, 5),(100, 6),
-- Mobile App developers
  (101, 5),
-- Billing System developers
  (102, 6),
-- Reporting Module developers
  (103, 5),(103, 6),
-- E-commerce Platform developers
  (104, 5),
-- CRM Tool developers
  (105, 6),
-- HR System developers
  (106, 5);
