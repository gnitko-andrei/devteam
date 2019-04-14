insert into user (id, username, password, active)
values (1, 'admin', '$2a$08$5GEDISDmjH1.agxI75qTf.lxMcJWMzq1bdy59hHlv9/3i4a/TOzGG', true);

insert into user_role (user_id, roles)
values (1, 'USER'),
       (1, 'ADMIN');
