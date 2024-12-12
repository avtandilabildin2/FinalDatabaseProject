CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       login VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(255) NOT NULL
);

CREATE TABLE messages (
                          id SERIAL PRIMARY KEY,
                          user_id INT REFERENCES users(id),
                          message TEXT NOT NULL,
                          sent TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
