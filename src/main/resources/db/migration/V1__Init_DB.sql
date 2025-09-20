CREATE TABLE hibernate_sequence
(
    next_val bigint
);

INSERT INTO
    hibernate_sequence
VALUES (2);

CREATE TABLE user
(
    id bigint NOT NULL,
    active bit NOT NULL,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    first_name varchar(255),
    last_name varchar(255),
    email varchar(255),
    contacts varchar(255),
    price integer,
    skills varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id bigint NOT NULL,
    roles varchar(255)
);

CREATE TABLE project
(
    id bigint NOT NULL,
    name varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    specification LONGTEXT,
    customer_id bigint,
    manager_id bigint,
    PRIMARY KEY (id)
);

CREATE TABLE developer_project
(
    developer_id bigint NOT NULL,
    project_id bigint NOT NULL,
    PRIMARY KEY (
        project_id,
        developer_id
    )
);

CREATE TABLE task
(
    id bigint NOT NULL,
    name varchar(255) NOT NULL,
    status varchar(255) NOT NULL,
    description LONGTEXT,
    time integer NOT NULL,
    price integer NOT NULL,
    project_id bigint,
    PRIMARY KEY (id)
);

ALTER TABLE user
  ADD CONSTRAINT user_username_uq UNIQUE (username);

ALTER TABLE user_role
  ADD CONSTRAINT user_role_user_fk
    FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE project
  ADD CONSTRAINT project_customer_fk
    FOREIGN KEY (customer_id) REFERENCES user (id);

ALTER TABLE project
  ADD CONSTRAINT project_manager_fk
    FOREIGN KEY (manager_id) REFERENCES user (id);

ALTER TABLE project
  ADD CONSTRAINT project_name_uq UNIQUE (name);

ALTER TABLE developer_project
  ADD CONSTRAINT developer_project_project_fk
    FOREIGN KEY (project_id) REFERENCES project (id);

ALTER TABLE developer_project
  ADD CONSTRAINT developer_project_developer_fk
    FOREIGN KEY (developer_id) REFERENCES user (id);

ALTER TABLE developer_project
  ADD CONSTRAINT ux_proj_dev UNIQUE (
    project_id,
    developer_id
);

ALTER TABLE task
  ADD CONSTRAINT task_project_fk
    FOREIGN KEY (project_id) REFERENCES project (id);
