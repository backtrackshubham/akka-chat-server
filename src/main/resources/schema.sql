CREATE TABLE IF NOT EXISTS users (
    id varchar(36) NOT NULL,
    firebase_id varchar(36) UNIQUE NOT NULL,
    name varchar(40) NOT NULL,
    email varchar(40) DEFAULT '',
    phone_number varchar(15) UNIQUE NOT NULL,
    is_active bool default true,
    CONSTRAINT PK_users PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS reg_users (
    phone_number varchar(15) NOT NULL PRIMARY KEY,
    email varchar(40) DEFAULT ''
);
