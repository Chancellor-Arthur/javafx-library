CREATE TABLE users
(
    id       serial
        CONSTRAINT users_pk PRIMARY KEY,
    login    varchar NOT NULL,
    password varchar,
    is_admin bool DEFAULT FALSE
);

CREATE TABLE books
(
    id          serial
        CONSTRAINT books_pk PRIMARY KEY,
    user_id     integer references users (id)
                            ON UPDATE CASCADE ON DELETE SET NULL,
    title       varchar NOT NULL,
    author      varchar,
    year        integer,
    pages       integer,
    description text,
    link        varchar NOT NULL
);

CREATE TABLE categories
(
    id   serial
        CONSTRAINT categories_pk PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE books_categories
(
    book_id     integer references books (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    category_id integer references categories (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (book_id, category_id)
);

CREATE TABLE logs
(
    id          serial
        CONSTRAINT logs_pk PRIMARY KEY,
    action      varchar NOT NULL,
    initiator_id integer,
    target_id    integer,
    old_value   json,
    new_value   json,
    created_at  timestamp with time zone default now()
);

CREATE OR REPLACE FUNCTION logging()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS
$audit$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO logs(action, initiator_id, target_id, old_value, new_value)
        VALUES (TG_OP, old.user_id, old.id, row_to_json(old), null);
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO logs(action, initiator_id, target_id, old_value, new_value)
        VALUES (TG_OP, new.user_id, new.id, row_to_json(old), row_to_json(new));
        RETURN NEW;
    ELSIF (TG_OP = 'INSERT') THEN
        INSERT INTO logs(action, initiator_id, target_id, old_value, new_value)
        VALUES (TG_OP, new.user_id, new.id, null, row_to_json(new));
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$audit$;

CREATE TRIGGER audit
AFTER INSERT OR UPDATE OR DELETE ON books
    FOR EACH ROW EXECUTE PROCEDURE logging();