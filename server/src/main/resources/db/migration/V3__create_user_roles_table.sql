CREATE TABLE user_roles
(
    id BIGINT,
    user_id UUID,
    role_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    PRIMARY KEY (id)
);
