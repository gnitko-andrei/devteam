create table developer_project
(
  developer_id bigint not null,
  project_id   bigint not null,
  primary key (project_id, developer_id)
);
create table hibernate_sequence
(
  next_val bigint
);
insert into hibernate_sequence
values (1);
insert into hibernate_sequence
values (1);
insert into hibernate_sequence
values (1);
create table project
(
  id            bigint       not null,
  name          varchar(255) not null,
  specification longtext,
  status        varchar(255) not null,
  customer_id   bigint,
  manager_id    bigint,
  primary key (id)
);
create table task
(
  id          bigint       not null,
  description longtext,
  name        varchar(255) not null,
  price       integer      not null,
  status      varchar(255) not null,
  time        integer      not null,
  project_id  bigint,
  primary key (id)
);
create table user
(
  id         bigint       not null,
  active     bit          not null,
  contacts   varchar(255),
  email      varchar(255),
  first_name varchar(255),
  last_name  varchar(255),
  password   varchar(255) not null,
  price      integer,
  skills     varchar(255),
  username   varchar(255) not null,
  primary key (id)
);
create table user_role
(
  user_id bigint not null,
  roles   varchar(255)
);
alter table user
  add constraint user_username_uq unique (username);
alter table developer_project
  add constraint developer_project_project_fk
    foreign key (project_id) references project (id);
alter table developer_project
  add constraint developer_project_developer_fk
    foreign key (developer_id) references user (id);
alter table project
  add constraint project_customer_fk
    foreign key (customer_id) references user (id);
alter table project
  add constraint project_manager_fk
    foreign key (manager_id) references user (id);
alter table task
  add constraint task_project_fk
    foreign key (project_id) references project (id);
alter table user_role
  add constraint user_role_user_fk
    foreign key (user_id) references user (id);
