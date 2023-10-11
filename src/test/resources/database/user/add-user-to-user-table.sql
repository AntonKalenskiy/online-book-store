ALTER TABLE users AUTO_INCREMENT = 1;

INSERT INTO users (email, password, first_name, last_name, shipping_address)
VALUES ('batman@gmail.org', '$2a$12$k19d2/swo89NSkUzxe62ROVcI5lhi1EliUKi208NEsko5h1KVQqLy',
'Bruce', 'Wayne', 'Gotham city, cave');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 6);


