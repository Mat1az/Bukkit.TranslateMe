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
    id   INTEGER PRIMARY KEY AUTOINCREMENT
                 NOT NULL,
    name         DEFAULT ""
                 NOT NULL,
    a    INTEGER REFERENCES color (id) ON DELETE CASCADE
                                       ON UPDATE CASCADE
                 NOT NULL,
    b    INTEGER REFERENCES color (id) ON DELETE CASCADE
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
    id       INTEGER
                     NOT NULL,
    language INTEGER REFERENCES language (id) ON DELETE CASCADE
                                              ON UPDATE CASCADE
                     NOT NULL
                     DEFAULT (0),
    value    TEXT    NOT NULL
                     DEFAULT "",
                     PRIMARY KEY (id, language)
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

INSERT INTO color (id,'value') VALUES(0,"#00FF00");
INSERT INTO color (id,'value') VALUES(1,"#c35353");
INSERT INTO color (id,'value') VALUES(2,"#FFFF00");
INSERT INTO color (id,'value') VALUES(3,"#dfda68");
INSERT INTO color (id,'value') VALUES(4,"#6a0d91");
INSERT INTO color (id,'value') VALUES(5,"#d9a8f4");
INSERT INTO color (id,'value') VALUES(6,"#f48fb1");
INSERT INTO color (id,'value') VALUES(7,"#fce4ec");
INSERT INTO color (id,'value') VALUES(8,"#8e24aa");
INSERT INTO color (id,'value') VALUES(9,"#e1bee7");
INSERT INTO color (id,'value') VALUES(10,"#1e88e5");
INSERT INTO color (id,'value') VALUES(11,"#e3f2fd");
INSERT INTO color (id,'value') VALUES(12,"#fdd835");
INSERT INTO color (id,'value') VALUES(13,"#fff9c4");
INSERT INTO color (id,'value') VALUES(14,"#ec407a");
INSERT INTO color (id,'value') VALUES(15,"#f8bbd0");
INSERT INTO color (id,'value') VALUES(16,"#b76e79");
INSERT INTO color (id,'value') VALUES(17,"#fce4ec");
INSERT INTO color_set (id,a,b) VALUES(0,0,1);
INSERT INTO color_set (id,a,b) VALUES(1,2,3);
INSERT INTO color_set (name,a,b) VALUES('Electric Violet',4,5);
INSERT INTO color_set (name,a,b) VALUES('Rose Quartz',6,7);
INSERT INTO color_set (name,a,b) VALUES('Nebula Purple',8,9);
INSERT INTO color_set (name,a,b) VALUES('Electric Blue',10,11);
INSERT INTO color_set (name,a,b) VALUES('Lemonade Yellow',12,13);
INSERT INTO color_set (name,a,b) VALUES('Electric Pink',14,15);
INSERT INTO color_set (name,a,b) VALUES('Rose Gold',16,17);
INSERT INTO 'language' (id,name) VALUES(0,"English");
INSERT INTO 'language' (id,name) VALUES(1,"Spanish");
INSERT INTO message (id,language,value) VALUES (0,0,'This is a test message.');
INSERT INTO message (id,language,value) VALUES (0,1,'Esto es un mensaje de prueba.');

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
