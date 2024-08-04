--
-- File generated with SQLiteStudio v3.4.4 on Sun Aug 4 07:35:38 2024
--
-- Text encoding used: UTF-8
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: color
CREATE TABLE IF NOT EXISTS color (
    id    INTEGER PRIMARY KEY AUTOINCREMENT
                  NOT NULL,
    value TEXT    NOT NULL
                  DEFAULT "",
    level INTEGER DEFAULT (0)
                  NOT NULL
);


-- Table: color_set
CREATE TABLE IF NOT EXISTS color_set (
    id INTEGER PRIMARY KEY AUTOINCREMENT
               NOT NULL,
    a  INTEGER REFERENCES color (id) ON DELETE CASCADE
                                     ON UPDATE CASCADE
               NOT NULL,
    b  INTEGER REFERENCES color (id) ON DELETE CASCADE
                                     ON UPDATE CASCADE
               NOT NULL
);


-- Table: language
CREATE TABLE IF NOT EXISTS language (
    id   INTEGER PRIMARY KEY AUTOINCREMENT
                 NOT NULL,
    name TEXT    NOT NULL
                 DEFAULT ""
);


-- Table: message
CREATE TABLE IF NOT EXISTS message (
    id       INTEGER PRIMARY KEY AUTOINCREMENT
                     NOT NULL,
    language INTEGER REFERENCES language (id) ON DELETE CASCADE
                                              ON UPDATE CASCADE
                     NOT NULL
                     DEFAULT (0),
    value    TEXT    NOT NULL
                     DEFAULT ""
);


-- Table: user
CREATE TABLE IF NOT EXISTS user (
    uuid     TEXT    PRIMARY KEY
                     NOT NULL,
    language INTEGER REFERENCES language (id) ON DELETE CASCADE
                                              ON UPDATE CASCADE
                     NOT NULL
                     DEFAULT (0)
);


-- Table: users_colors
CREATE TABLE IF NOT EXISTS users_colors (
    user TEXT    REFERENCES user (uuid) ON DELETE CASCADE
                                        ON UPDATE CASCADE
                 PRIMARY KEY
                 NOT NULL,
    a    INTEGER REFERENCES color_set (id) ON DELETE CASCADE
                                           ON UPDATE CASCADE
                 NOT NULL
                 DEFAULT (0),
    b    INTEGER REFERENCES color_set (id) ON DELETE CASCADE
                                           ON UPDATE CASCADE
                 NOT NULL
                 DEFAULT (1),
    c    INTEGER REFERENCES color_set (id) ON DELETE CASCADE
                                           ON UPDATE CASCADE
                 NOT NULL
                 DEFAULT (2)
);

-- Trigger: create_user
CREATE TRIGGER IF NOT EXISTS create_user
                      BEFORE INSERT
                          ON users_colors
                    FOR EACH ROW
BEGIN
    INSERT OR REPLACE INTO user (
                                    uuid
                                )
                                VALUES (
                                    NEW.user
                                );
END;

INSERT INTO color (id,'value') VALUES(0,"#00FF00");
INSERT INTO color (id,'value') VALUES(1,"#c35353");
INSERT INTO color (id,'value') VALUES(2,"#FFFF00");
INSERT INTO color (id,'value') VALUES(3,"#dfda68");
INSERT INTO color (id,'value') VALUES(4,"#A020F0");
INSERT INTO color (id,'value') VALUES(5,"#9174a9");
INSERT INTO color_set (id,a,b) VALUES(0,0,1);
INSERT INTO color_set (id,a,b) VALUES(1,2,3);
INSERT INTO color_set (id,a,b) VALUES(2,4,5);
INSERT INTO 'language' (id,name) VALUES(0,"English");

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
