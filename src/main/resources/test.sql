create table if not exists USERS
(
    USER_ID BIGINT generated by default as identity not null,
    NAME    VARCHAR(255) NOT NULL,
    EMAIL   VARCHAR(200) NOT NULL,
    constraint UQ_USER_EMAIL UNIQUE (EMAIL),
    constraint USERS_PK
        primary key (USER_ID)
);

create table if not exists REQUESTS
(
    ID           BIGINT generated by default as identity not null,
    DESCRIPTION  CHARACTER VARYING,
    REQUESTOR_ID BIGINT not null,
    CREATED  TIMESTAMP WITHOUT TIME ZONE not null,
    constraint REQUESTS_PK
        primary key (ID)
);

create table if not exists ITEMS
(
    ITEM_ID     BIGINT generated by default as identity not null,
    NAME        CHARACTER VARYING(255) not null,
    DESCRIPTION CHARACTER VARYING(500) not null,
    AVAILABLE   BOOLEAN                not null,
    OWNER_ID    BIGINT                 not null,
    REQUEST_ID  BIGINT,
    constraint ITEMS_PK
        primary key (ITEM_ID)
);

create table if not exists BOOKINGS
(
    ID BIGINT generated by default as identity     not null,
    ITEM_ID    BIGINT                              not null,
    OWNER_ID   BIGINT                              not null,
    START_TIME  TIMESTAMP WITHOUT TIME ZONE        not null,
    END_TIME  TIMESTAMP WITHOUT TIME ZONE          not null,
    STATUS     CHARACTER VARYING default 'WAITING' not null,
    USER_ID    BIGINT                              not null,
    constraint BOOKINGS_PK
        primary key (ID)
);

create table if not exists COMMENTS
(
    ID        BIGINT generated by default as identity not null
        primary key,
    TEXT      CHARACTER VARYING not null,
    ITEM_ID   BIGINT            not null,
    AUTHOR_ID BIGINT            not null,
    CREATED  TIMESTAMP WITHOUT TIME ZONE        not null
);