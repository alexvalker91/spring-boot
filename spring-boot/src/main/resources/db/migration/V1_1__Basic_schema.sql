CREATE TABLE event_db (
    id SERIAL PRIMARY KEY,
    event_title VARCHAR(255) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    event_ticket_price INTEGER
);

CREATE TABLE user_db (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL
);

CREATE TYPE ticket_category AS ENUM ('STANDARD', 'PREMIUM', 'BAR');

CREATE TABLE ticket_db (
    id SERIAL PRIMARY KEY,
    event_id INT REFERENCES event_db(id),
    user_id INT REFERENCES user_db(id),
    category ticket_category,
    ticket_place INTEGER
);

CREATE TABLE user_account_db (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES user_db(id),
    user_amount INTEGER
);