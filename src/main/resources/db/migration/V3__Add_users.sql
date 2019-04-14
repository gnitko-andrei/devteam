# customers


insert into user (id, username, password, active, first_name, last_name, email, contacts)
values (2,
        'customer',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'customerFirstName',
        'customerLastName',
        'customer@email',
        'Customer contacts');
insert into user_role (user_id, roles)
values (2, 'USER'),
       (2, 'CUSTOMER');


insert into user (id, username, password, active, first_name, last_name, email, contacts)
values (3,
        'customer1',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'customer1FirstName',
        'customer1LastName',
        'customer1@email',
        'Customer1 contacts');
insert into user_role (user_id, roles)
values (3, 'USER'),
       (3, 'CUSTOMER');


insert into user (id, username, password, active, first_name, last_name, email, contacts)
values (4,
        'customer2',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'customer2FirstName',
        'customer2LastName',
        'customer2@email',
        'Customer2 contacts');
insert into user_role (user_id, roles)
values (4, 'USER'),
       (4, 'CUSTOMER');


# developers


insert into user (id, username, password, active, first_name, last_name, email, contacts, price, skills)
values (5,
        'developer',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'developerFirstName',
        'developerLastName',
        'developer@email',
        'Developer contacts',
        3,
        'Developer skills');
insert into user_role (user_id, roles)
values (5, 'USER'),
       (5, 'DEVELOPER');


insert into user (id, username, password, active, first_name, last_name, email, contacts, price, skills)
values (6,
        'developer1',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'developer1FirstName',
        'developer1LastName',
        'developer1@email',
        'Developer1 contacts',
        3,
        'Developer1 skills');
insert into user_role (user_id, roles)
values (6, 'USER'),
       (6, 'DEVELOPER');


insert into user (id, username, password, active, first_name, last_name, email, contacts, price, skills)
values (7,
        'developer2',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'developer2FirstName',
        'developer2LastName',
        'developer2@email',
        'Developer2 contacts',
        3,
        'Developer2 skills');
insert into user_role (user_id, roles)
values (7, 'USER'),
       (7, 'DEVELOPER');


insert into user (id, username, password, active, first_name, last_name, email, contacts, price, skills)
values (8,
        'developer3',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'developer3FirstName',
        'developer3LastName',
        'developer3@email',
        'Developer3 contacts',
        3,
        'Developer3 skills');
insert into user_role (user_id, roles)
values (8, 'USER'),
       (8, 'DEVELOPER');


insert into user (id, username, password, active, first_name, last_name, email, contacts, price, skills)
values (9,
        'developer4',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'developer4FirstName',
        'developer4LastName',
        'developer4@email',
        'Developer4 contacts',
        3,
        'Developer4 skills');
insert into user_role (user_id, roles)
values (9, 'USER'),
       (9, 'DEVELOPER');


insert into user (id, username, password, active, first_name, last_name, email, contacts, price, skills)
values (10,
        'developer5',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'developer5FirstName',
        'developer5LastName',
        'developer5@email',
        'Developer5 contacts',
        3,
        'Developer5 skills');
insert into user_role (user_id, roles)
values (10, 'USER'),
       (10, 'DEVELOPER');


# managers


insert into user (id, username, password, active, first_name, last_name, email, contacts)
values (11,
        'manager',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'managerFirstName',
        'managerLastName',
        'manager@email',
        'Manager contacts');
insert into user_role (user_id, roles)
values (11, 'USER'),
       (11, 'MANAGER');


insert into user (id, username, password, active, first_name, last_name, email, contacts)
values (12,
        'manager1',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'manager1FirstName',
        'manager1LastName',
        'manager1@email',
        'Manager1 contacts');
insert into user_role (user_id, roles)
values (12, 'USER'),
       (12, 'MANAGER');


insert into user (id, username, password, active, first_name, last_name, email, contacts)
values (13,
        'manager2',
        '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG',
        true,
        'manager2FirstName',
        'manager2LastName',
        'manager2@email',
        'Manager2 contacts');
insert into user_role (user_id, roles)
values (13, 'USER'),
       (13, 'MANAGER');



drop table hibernate_sequence;
create table hibernate_sequence
(
  next_val bigint
);
insert into hibernate_sequence
values (14);