INSERT INTO
	user (id, active, username, password, first_name, last_name, email, contacts, price, skills)
VALUES
  (1, TRUE, 'testuser', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Anna', 'Kowalska', 'anna.kowalska@example.com', 'tel:+48 600 111 111', NULL, NULL),
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