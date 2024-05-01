CREATE TABLE users
(
    user_name VARCHAR(64) PRIMARY KEY,
    followers INT NOT NULL,
    following INT NOT NULL
);

INSERT INTO users (user_name, followers, following)
VALUES ('@user1', 0, 0);
INSERT INTO users (user_name, followers, following)
VALUES ('@user2', 0, 0);
INSERT INTO users (user_name, followers, following)
VALUES ('@user3', 0, 0);
INSERT INTO users (user_name, followers, following)
VALUES ('@user4', 0, 0);
INSERT INTO users (user_name, followers, following)
VALUES ('@user5', 0, 0);

