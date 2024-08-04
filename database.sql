PRAGMA foreign_keys = on;
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
                     DEFAULT (0),
    color    INTEGER REFERENCES color (id) ON DELETE CASCADE
                                           ON UPDATE CASCADE
                     DEFAULT (0) 
                     NOT NULL
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


COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
