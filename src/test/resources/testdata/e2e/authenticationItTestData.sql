INSERT INTO
	user (id, active, username, password, first_name, last_name, email, contacts, price, skills)
VALUES
  (1, TRUE, 'testuser', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', 'Anna', 'Kowalska', 'anna.kowalska@example.com', 'tel:+48 600 111 111', NULL, NULL);

  -- Roles mapping
  INSERT INTO user_role (user_id, roles)
  VALUES
    (1, 'USER');