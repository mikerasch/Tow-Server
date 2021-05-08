/*
 Creates a very basic file storage table that will overwrite the previous image when a new one is entered.
 */

create table public.file_db
(
    id   uuid NOT NULL,
    type VARCHAR(255),
    data bytea
)
    TABLESPACE pg_default;

CREATE INDEX file_db_id ON file_db (id);

-- Insert DB entry
CREATE FUNCTION create_file_db_entry()
    RETURNS trigger AS
$$
BEGIN
    INSERT INTO file_db(id)
    VALUES (NEW.id);

    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;

-- Delete DB entry
CREATE FUNCTION delete_file_db_entry()
    RETURNS trigger AS
$$
BEGIN
    DELETE
    FROM file_db
    where id = OLD.id;

    RETURN OLD;
END;
$$
    LANGUAGE plpgsql;

-- Create Insert Trigger Users
CREATE TRIGGER create_file_db_entry
    AFTER INSERT
    ON users
    FOR ROW
EXECUTE PROCEDURE create_file_db_entry();

CREATE TRIGGER delete_file_db_entry
    AFTER DELETE
    ON users
    FOR ROW
EXECUTE PROCEDURE delete_file_db_entry();

-- Create Insert Trigger tc_admin
CREATE TRIGGER create_file_db_entry
    AFTER INSERT
    ON tc_admin
    FOR ROW
EXECUTE PROCEDURE create_file_db_entry();

CREATE TRIGGER delete_file_db_entry
    AFTER DELETE
    ON tc_admin
    FOR ROW
EXECUTE PROCEDURE delete_file_db_entry();

-- Create Insert Trigger tc_user
CREATE TRIGGER create_file_db_entry
    AFTER INSERT
    ON tc_user
    FOR ROW
EXECUTE PROCEDURE create_file_db_entry();

CREATE TRIGGER delete_file_db_entry
    AFTER DELETE
    ON tc_user
    FOR ROW
EXECUTE PROCEDURE delete_file_db_entry();

-- Create Insert Trigger pd_admin
CREATE TRIGGER create_file_db_entry
    AFTER INSERT
    ON pd_admin
    FOR ROW
EXECUTE PROCEDURE create_file_db_entry();

CREATE TRIGGER delete_file_db_entry
    AFTER DELETE
    ON pd_admin
    FOR ROW
EXECUTE PROCEDURE delete_file_db_entry();

-- Create Insert Trigger pd_user
CREATE TRIGGER create_file_db_entry
    AFTER INSERT
    ON pd_user
    FOR ROW
EXECUTE PROCEDURE create_file_db_entry();

CREATE TRIGGER delete_file_db_entry
    AFTER DELETE
    ON pd_user
    FOR ROW
EXECUTE PROCEDURE delete_file_db_entry();