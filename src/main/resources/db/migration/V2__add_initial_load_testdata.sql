-- Password hash for all users: "1"
-- $2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG

-- Clean existing data
DELETE FROM developer_project;
DELETE FROM task;
DELETE FROM project;
DELETE FROM user_role;
DELETE FROM user;

-- Users (admins, customers, managers, developers, user)
INSERT INTO user (id, active, contacts, email, first_name, last_name, password, price, skills, username) VALUES
  (1, TRUE, 'tel:+48 600 111 111', 'anna.kowalska@example.com', 'Anna', 'Kowalska', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', NULL, NULL, 'admin'),
  (2, TRUE, 'tel:+48 600 222 222', 'piotr.nowak@example.com',   'Piotr','Nowak',    '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', NULL, NULL, 'customer'),
  (3, TRUE, 'tel:+48 600 333 333', 'maria.wisniewska@example.com','Maria','Wiśniewska','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', NULL, NULL, 'manager'),
  (4, TRUE, 'tel:+48 600 444 444', 'jakub.zielinski@example.com','Jakub','Zieliński','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 120, 'Java, Spring Boot', 'developer'),
  (5, TRUE, 'tel:+48 600 555 555', 'aleksandra.mazur@example.com','Aleksandra','Mazur','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', NULL, NULL, 'user'),
  (6, TRUE, 'tel:+48 600 666 666', 'jan.krawczyk@example.com','Jan','Krawczyk','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', NULL, NULL, 'customer2'),
  (7, TRUE, 'tel:+48 600 777 777', 'ewelina.dabrowska@example.com','Ewelina','Dąbrowska','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', NULL, NULL, 'customer3'),
  (8, TRUE, 'tel:+48 600 888 888', 'tomasz.wojcik@example.com','Tomasz','Wójcik','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', NULL, NULL, 'manager2'),
  (9, TRUE, 'tel:+48 600 999 999', 'magda.lewandowska@example.com','Magda','Lewandowska','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', NULL, NULL, 'manager3'),
  (10, TRUE, 'tel:+48 600 123 456','filip.kaczmarek@example.com','Filip','Kaczmarek','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 110,'React, REST','developer2'),
  (11, TRUE, 'tel:+48 600 234 567','agnieszka.pawlak@example.com','Agnieszka','Pawlak','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 130,'SQL, Docker','developer3'),
  (12, TRUE, 'tel:+48 600 345 678','karol.adamczyk@example.com','Karol','Adamczyk','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 115,'Spring, Kafka','developer4'),
  (13, TRUE, 'tel:+48 600 456 789','paulina.gorska@example.com','Paulina','Górska','$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 125,'Frontend, Angular','developer5');

-- Roles mapping
INSERT INTO user_role (user_id, roles) VALUES
  (1, 'USER'), (1, 'ADMIN'),
  (2, 'USER'), (2, 'CUSTOMER'),
  (3, 'USER'), (3, 'MANAGER'),
  (4, 'USER'), (4, 'DEVELOPER'),
  (5, 'USER'),
  (6, 'USER'), (6, 'CUSTOMER'),
  (7, 'USER'), (7, 'CUSTOMER'),
  (8, 'USER'), (8, 'MANAGER'),
  (9, 'USER'), (9, 'MANAGER'),
  (10,'USER'), (10,'DEVELOPER'),
  (11,'USER'), (11,'DEVELOPER'),
  (12,'USER'), (12,'DEVELOPER'),
  (13,'USER'), (13,'DEVELOPER');

-- Projects with multi-paragraph specifications
INSERT INTO project (id, name, specification, status, customer_id, manager_id) VALUES
  (100, 'Customer Portal', 'As a customer, I want to log in securely, update my personal data, and track all my orders in one place, so that I can manage my account without calling support.\n\nAcceptance criteria:\n- Login and registration forms with email verification\n- Profile management with contact info and preferences\n- Order history with filtering by date and status\n\nAdditional notes:\n- Must comply with GDPR\n- Needs support for multi-language UI', 'NEW', 2, 3),
  (101, 'Mobile App', 'As a busy professional, I want a mobile app that works both offline and online, notifies me about task updates, and synchronizes automatically, so that I stay productive on the go.\n\nAcceptance criteria:\n- Offline caching of tasks and time logs\n- Push notifications for deadlines and assignments\n- Automatic sync with server when online\n\nAdditional notes:\n- Must run on both iOS and Android\n- Should follow platform UI guidelines', 'PENDING_MANAGER', 2, 3),
  (102, 'Billing System', 'As a finance officer, I want the system to generate invoices with VAT, discounts, and currency conversion, and send them via email to clients, so that billing is automated and accurate.\n\nAcceptance criteria:\n- Automatic invoice generation on task completion\n- Support multiple currencies with real-time conversion\n- Email distribution of invoices to customers\n\nAdditional notes:\n- Integrate with external accounting system\n- Provide detailed audit logs for all transactions', 'IN_PROGRESS', 2, 3),
  (103, 'Reporting Module', 'As a manager, I want dashboards and scheduled reports that summarize team performance and KPIs, so that I can make informed decisions quickly.\n\nAcceptance criteria:\n- Interactive charts with filters\n- Export to PDF and Excel\n- Weekly email reports\n\nAdditional notes:\n- Reports should be customizable by role\n- Data refresh should be near real-time', 'COMPLETED', 2, 3),
  (104, 'E-commerce Platform', 'As a shopper, I want to search products, read reviews, and checkout securely, so that I can purchase confidently.\n\nAcceptance criteria:\n- Product catalog with categories and filters\n- Shopping cart and wishlist\n- Payment via card and PayPal\n\nAdditional notes:\n- Support discount codes\n- Track stock availability and backorders', 'NEW', 6, 8),
  (105, 'CRM Tool', 'As a sales team, we want a CRM to track leads and automate follow-ups, so that we can close deals faster.\n\nAcceptance criteria:\n- Lead pipeline board\n- Automated reminders and notifications\n- Email integration\n\nAdditional notes:\n- Provide role-based permissions\n- Support exporting leads to CSV', 'IN_PROGRESS', 6, 8),
  (106, 'HR System', 'As an HR manager, I want to manage employee data and payroll, so that HR processes are streamlined.\n\nAcceptance criteria:\n- Employee profiles with personal and job info\n- Payroll calculation with deductions and bonuses\n- Leave request and approval workflow\n\nAdditional notes:\n- Include performance review history\n- Integrate with external payroll services', 'PENDING_MANAGER', 7, 9),
  (107, 'Logistics Tracker', 'As a logistics coordinator, I want to track shipments in real-time, so that I can prevent delays.\n\nAcceptance criteria:\n- Map integration with delivery routes\n- Real-time tracking of shipment statuses\n- Alerts for delays\n\nAdditional notes:\n- Provide analytics for delivery times\n- Support multiple carriers', 'COMPLETED', 7, 9);

-- Developer assignments
INSERT INTO developer_project (project_id, developer_id) VALUES
  -- Customer Portal developers
  (100, 4), (100, 10), (100, 11), (100, 12), (100, 13),
  -- Mobile App developers
  (101, 4), (101, 11), (101, 12), (101, 13),
  -- Billing System developers
  (102, 10), (102, 11), (102, 12), (102, 13),
  -- Reporting Module developers
  (103, 4), (103, 10), (103, 12), (103, 13),
  -- E-commerce Platform developers
  (104, 10), (104, 11), (104, 12), (104, 13),
  -- CRM Tool developers
  (105, 11), (105, 12), (105, 13),
  -- HR System developers
  (106, 4), (106, 10), (106, 12), (106, 13),
  -- Logistics Tracker developers
  (107, 11), (107, 12), (107, 13);

-- Tasks with verbose multi-line descriptions
INSERT INTO task (id, name, description, price, status, time, project_id) VALUES
  -- Project 100
  (2000,'Gather requirements','As a business analyst, I need to conduct interviews, collect user stories, and document them in Confluence so that the team understands the scope.\n\nAcceptance criteria:\n- At least 5 stakeholder interviews completed\n- User stories documented and approved by customer\n\nAdditional notes:\n- Use Miro for workflow diagrams',520,'NEW',5,100),
  (2001,'Design UI mockups','As a designer, I want to create Figma mockups with responsive breakpoints and accessibility guidelines so that developers have a clear visual reference.\n\nAcceptance criteria:\n- Desktop and mobile versions\n- Color contrast compliant with WCAG 2.1\n\nAdditional notes:\n- Include dark mode variant',640,'NEW',6,100),
  (2002,'Setup DB schema','As a backend developer, I want normalized DB tables with Flyway migrations so that the application has a stable data foundation.\n\nAcceptance criteria:\n- User, project, and task tables created\n- Constraints and indexes applied\n\nAdditional notes:\n- Add seed data for local testing',730,'IN_PROGRESS',7,100),
  (2003,'Implement login','As a user, I want to log in securely with email and password so that I can access the portal safely.\n\nAcceptance criteria:\n- Passwords hashed with BCrypt\n- Error messages for failed logins\n\nAdditional notes:\n- Account lockout after 5 failed attempts',870,'IN_PROGRESS',8,100),
  (2004,'Deploy prototype','As a DevOps engineer, I want to deploy the prototype to staging with Docker Compose so that testers can try features early.\n\nAcceptance criteria:\n- Accessible at staging URL\n- Basic smoke tests pass\n\nAdditional notes:\n- Prepare rollback instructions',450,'DONE',4,100),
  (2005,'Accessibility audit','As a QA engineer, I want to test for WCAG compliance so that the app is usable by everyone.\n\nAcceptance criteria:\n- Screen reader navigation works\n- Color contrast verified\n\nAdditional notes:\n- Include keyboard navigation tests',400,'NEW',4,100),
  (2006,'Error logging','As a developer, I want centralized logging with SLF4J and ELK so that errors are traceable.\n\nAcceptance criteria:\n- Errors logged with stacktrace\n- Logs visible in Kibana\n\nAdditional notes:\n- Mask sensitive fields',600,'NEW',6,100),

  -- Project 101
  (2010,'API design','As a developer, I want OpenAPI docs for all endpoints so that teams can integrate in parallel.\n\nAcceptance criteria:\n- Endpoints documented\n- Examples provided\n\nAdditional notes:\n- Review by FE/BE leads',540,'NEW',5,101),
  (2011,'Authentication','As a user, I want secure token-based authentication with JWT so that I stay logged in safely.\n\nAcceptance criteria:\n- Token refresh implemented\n- Expired tokens rejected\n\nAdditional notes:\n- Store tokens securely client-side',710,'IN_PROGRESS',7,101),
  (2012,'Push notifications','As a mobile user, I want push alerts for deadlines so that I never miss important updates.\n\nAcceptance criteria:\n- Notifications on Android & iOS\n- Delivered within 5 seconds\n\nAdditional notes:\n- Support deep links',920,'NEW',9,101),
  (2013,'Offline mode','As a user, I want offline caching and auto-sync so that I can work without internet.\n\nAcceptance criteria:\n- Tasks saved locally\n- Auto-sync on reconnect\n\nAdditional notes:\n- Handle conflicts gracefully',650,'NEW',6,101),
  (2014,'App store release','As a release manager, I want builds and assets prepared for App Store and Google Play so that the app can be published.\n\nAcceptance criteria:\n- Metadata filled\n- TestFlight build uploaded\n\nAdditional notes:\n- Comply with store guidelines',1020,'IN_PROGRESS',10,101),
  (2015,'UI testing','As QA, I want automated UI tests with Selenium so that regressions are caught.\n\nAcceptance criteria:\n- Login and task screens covered\n- Tests pass in CI/CD\n\nAdditional notes:\n- Integrate with GitHub Actions',800,'NEW',8,101),

  -- Project 102
  (2020,'Invoice entity','As a developer, I want JPA entities for invoices so that data is consistent.\n\nAcceptance criteria:\n- Validation annotations added\n- Relationships mapped\n\nAdditional notes:\n- Include created_at/updated_at',430,'IN_PROGRESS',4,102),
  (2021,'Billing rules','As a finance manager, I want automatic discounts and taxes so that invoices match policy.\n\nAcceptance criteria:\n- Discounts by code\n- VAT by country\n\nAdditional notes:\n- Configurable tax rules',820,'IN_PROGRESS',8,102),
  (2022,'Kafka integration','As a developer, I want billing events in Kafka so that other services can react.\n\nAcceptance criteria:\n- Events published to topic\n- Consumer reads them\n\nAdditional notes:\n- Use Avro schemas',750,'NEW',7,102),
  (2023,'Unit tests','As QA, I want >80% coverage on billing services so that regressions are prevented.\n\nAcceptance criteria:\n- Services covered\n- External calls mocked\n\nAdditional notes:\n- Coverage report in CI',670,'IN_PROGRESS',6,102),
  (2024,'REST endpoints','As a developer, I want CRUD endpoints for invoices so that external apps can integrate.\n\nAcceptance criteria:\n- GET/POST/PUT/DELETE implemented\n- Validation errors handled\n\nAdditional notes:\n- Proper HTTP statuses',940,'IN_PROGRESS',9,102),
  (2025,'CI/CD setup','As DevOps, I want GitHub Actions pipelines so that builds are automated.\n\nAcceptance criteria:\n- Build/test/deploy steps\n- Status badges in repo\n\nAdditional notes:\n- Notifications on failure',900,'NEW',9,102),

  -- Project 103
  (2030,'Dashboard charts','As a manager, I want KPI charts so that I can analyze performance quickly.\n\nAcceptance criteria:\n- Bar & line charts\n- Date range filters\n\nAdditional notes:\n- Export as PNG',510,'DONE',5,103),
  (2031,'PDF export','As a user, I want PDF downloads so that I can share reports.\n\nAcceptance criteria:\n- Fonts embedded\n- Charts included\n\nAdditional notes:\n- Optional password protection',620,'DONE',6,103),
  (2032,'Role checks','As a system, I want RBAC enforced for reports so that data is protected.\n\nAcceptance criteria:\n- Roles checked per request\n- Unauthorized access blocked\n\nAdditional notes:\n- Add audit logs',420,'DONE',4,103),
  (2033,'Email reports','As a manager, I want weekly reports by email so that I stay informed.\n\nAcceptance criteria:\n- Sent Monday 9:00\n- Correct recipients',530,'DONE',5,103),
  (2034,'UI polish','As a frontend dev, I want to refine spacing, font sizes, and colors so that the UI is accessible and clean.\n\nAcceptance criteria:\n- Consistent styles\n- Meets design system\n\nAdditional notes:\n- Follow WCAG guidance',310,'DONE',3,103),

  -- Project 104
  (2040,'Product catalog','As a shopper, I want a categorized catalog with search so I can find items quickly.\n\nAcceptance criteria:\n- Category filter works\n- Accurate search results\n\nAdditional notes:\n- Consider synonyms',630,'NEW',6,104),
  (2041,'Shopping cart','As a shopper, I want to manage cart items so that checkout is simple.\n\nAcceptance criteria:\n- Add/remove/update items\n- Dynamic totals\n\nAdditional notes:\n- Persist cart for guests',740,'NEW',7,104),
  (2042,'Payment gateway','As a user, I want secure payments so that my transactions are safe.\n\nAcceptance criteria:\n- Card & PayPal supported\n- 3D Secure flow\n\nAdditional notes:\n- Handle payment webhooks',950,'IN_PROGRESS',9,104),
  (2043,'Order history','As a shopper, I want to view past orders so that I can track purchases.\n\nAcceptance criteria:\n- Chronological list\n- Status per order\n\nAdditional notes:\n- Download invoice PDF',520,'NEW',5,104),
  (2044,'Wishlist','As a shopper, I want to save items to a wishlist so that I can revisit them later.\n\nAcceptance criteria:\n- Items persist across sessions\n- Easy add/remove\n\nAdditional notes:\n- Shareable wishlist link',410,'NEW',4,104),
  (2045,'Product reviews','As a shopper, I want to write reviews so that I can share my experience.\n\nAcceptance criteria:\n- Star rating + comment\n- Reviews visible on product page\n\nAdditional notes:\n- Moderation workflow',600,'NEW',6,104),

  -- Project 105
  (2050,'CRM contacts','As a sales rep, I want to manage contacts so that all interactions are tracked.\n\nAcceptance criteria:\n- Add/edit forms\n- Call/email logs attached\n\nAdditional notes:\n- Import from CSV',560,'IN_PROGRESS',5,105),
  (2051,'Sales pipeline','As a sales manager, I want a visual pipeline so that I can track deals easily.\n\nAcceptance criteria:\n- Kanban stages configurable\n- Drag-and-drop between stages\n\nAdditional notes:\n- Forecast per stage',780,'IN_PROGRESS',7,105),
  (2052,'Email integration','As a user, I want email sync with Gmail/Outlook so that communication is centralized.\n\nAcceptance criteria:\n- OAuth2 connection\n- Two-way sync\n\nAdditional notes:\n- Respect rate limits',640,'NEW',6,105),
  (2053,'Reports','As a manager, I want customizable sales reports so that I can analyze pipeline health.\n\nAcceptance criteria:\n- Filters and charts\n- Export to CSV/PDF\n\nAdditional notes:\n- Save report presets',810,'NEW',8,105),
  (2054,'Notifications','As a user, I want alerts for new leads and task assignments so that I can follow up quickly.\n\nAcceptance criteria:\n- In-app + email notifications\n- Quiet hours setting\n\nAdditional notes:\n- Notification center page',450,'NEW',4,105),
  (2055,'GDPR compliance','As compliance, I want data privacy features so that we meet regulations.\n\nAcceptance criteria:\n- Right to be forgotten\n- Data export for user\n\nAdditional notes:\n- Audit consent changes',900,'IN_PROGRESS',9,105),
  (2056,'Calendar sync','As a user, I want calendar integration so that meetings appear automatically.\n\nAcceptance criteria:\n- Google/Microsoft sync\n- Invite attendees\n\nAdditional notes:\n- Timezone handling',650,'NEW',6,105),
  (2057,'User roles','As an admin, I want fine-grained permissions so that data is secure.\n\nAcceptance criteria:\n- Roles & scopes\n- Permission checks in API\n\nAdditional notes:\n- Admin UI for roles',720,'NEW',7,105),
  (2058,'REST API','As a partner, I want a REST API so that I can integrate external systems.\n\nAcceptance criteria:\n- OAuth2 client credentials\n- Rate limiting\n\nAdditional notes:\n- Publish API docs',800,'IN_PROGRESS',8,105),

  -- Project 106
  (2060,'Employee profiles','As HR, I want detailed employee profiles so that information is centralized.\n\nAcceptance criteria:\n- Personal & job info\n- Document uploads\n\nAdditional notes:\n- Field-level permissions',670,'IN_PROGRESS',6,106),
  (2061,'Leave requests','As an employee, I want to request leave online so that approvals are traceable.\n\nAcceptance criteria:\n- Request workflow\n- Manager approvals\n\nAdditional notes:\n- Calendar view of absences',720,'NEW',7,106),
  (2062,'Payroll','As HR, I want automated salary calculation so that payroll is accurate.\n\nAcceptance criteria:\n- Allowances & deductions\n- Payslip generation\n\nAdditional notes:\n- Export to accounting',930,'NEW',9,106),
  (2063,'Attendance','As HR, I want to track attendance so that compliance is ensured.\n\nAcceptance criteria:\n- Clock-in/out\n- Overtime rules\n\nAdditional notes:\n- Geo-fencing support',560,'NEW',5,106),
  (2064,'Performance','As a manager, I want performance reviews and goals so that development is measurable.\n\nAcceptance criteria:\n- Review cycles\n- Goal tracking\n\nAdditional notes:\n- 360° feedback option',490,'NEW',4,106),
  (2065,'Org chart','As HR, I want an org chart so that reporting lines are visible.\n\nAcceptance criteria:\n- Interactive org tree\n- Export as image\n\nAdditional notes:\n- Drag-and-drop reorg',800,'NEW',8,106),
  (2066,'Notifications','As HR, I want employee notifications so that updates are communicated.\n\nAcceptance criteria:\n- Email templates\n- In-app inbox\n\nAdditional notes:\n- A/B test messaging',400,'NEW',4,106),
  (2067,'Training module','As L&D, I want courses and quizzes so that employees can upskill.\n\nAcceptance criteria:\n- Course catalog\n- Quiz scoring\n\nAdditional notes:\n- Certificates on completion',700,'NEW',7,106),
  (2068,'Recruitment','As HR, I want job postings and candidate tracking so that hiring is organized.\n\nAcceptance criteria:\n- Job board\n- Candidate pipeline\n\nAdditional notes:\n- CV parsing',750,'NEW',7,106),

  -- Project 107
  (2070,'Shipment API','As a developer, I want a REST API for shipments so that logistics apps can integrate.\n\nAcceptance criteria:\n- Status endpoint\n- Auth via API key\n\nAdditional notes:\n- Versioned API',760,'DONE',7,107),
  (2071,'Maps integration','As a user, I want to see delivery routes on a map so that I understand progress.\n\nAcceptance criteria:\n- Map tiles loaded\n- Route polylines\n\nAdditional notes:\n- Cache map tiles',640,'DONE',6,107),
  (2072,'Notifications','As a customer, I want SMS/email alerts about shipment status so that I stay informed.\n\nAcceptance criteria:\n- Event-based triggers\n- Opt-in preferences\n\nAdditional notes:\n- Unsubscribe links',530,'DONE',5,107),
  (2073,'Analytics','As a manager, I want delivery analytics so that bottlenecks are visible.\n\nAcceptance criteria:\n- Avg delivery time KPI\n- Delay heatmap\n\nAdditional notes:\n- Export to CSV',820,'DONE',8,107),
  (2074,'User portal','As a customer, I want a portal to see my orders and shipment statuses so that I have transparency.\n\nAcceptance criteria:\n- Order list & search\n- Status timeline\n\nAdditional notes:\n- Mobile-friendly layout',910,'DONE',9,107),
  (2075,'Barcode scanning','As a courier, I want barcode scanning so that package updates are quick.\n\nAcceptance criteria:\n- Camera scan\n- Error feedback\n\nAdditional notes:\n- Offline queue',600,'DONE',6,107),
  (2076,'Audit logs','As compliance, I want audit logs so that changes are traceable.\n\nAcceptance criteria:\n- Who/when/what stored\n- Tamper-evident\n\nAdditional notes:\n- Retention policy',700,'DONE',7,107),
  (2077,'Integration tests','As QA, I want end-to-end tests so that critical flows are verified.\n\nAcceptance criteria:\n- Happy-path scenarios\n- Failure cases\n\nAdditional notes:\n- Run nightly in CI',650,'DONE',6,107),
  (2078,'Notifications hub','As a user, I want a unified notification center so that all alerts are in one place.\n\nAcceptance criteria:\n- Read/unread state\n- Filters\n\nAdditional notes:\n- Snooze notifications',500,'DONE',5,107);

-- Update sequence
UPDATE hibernate_sequence SET next_val = 13000;