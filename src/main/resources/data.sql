-- DELETE FROM roles WHERE name = 'ROLE_USER' AND id NOT IN (
--     SELECT MIN(id) FROM roles WHERE name = 'ROLE_USER'
-- );

-- INSERT INTO roles (name) VALUES ('ROLE_USER');
-- INSERT INTO roles (name) VALUES ('ROLE_ADMIN');


