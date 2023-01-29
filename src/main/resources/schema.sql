create table if not exists USERS
(
    USER_ID BIGINT generated by default as identity not null,
    NAME    VARCHAR(255) NOT NULL,
    EMAIL   VARCHAR(200) NOT NULL,
    constraint UQ_USER_EMAIL UNIQUE (EMAIL),
    constraint USERS_PK
        primary key (USER_ID)
);

create table if not exists ITEMS
(
    ITEM_ID     BIGINT generated by default as identity not null,
    NAME        CHARACTER VARYING(255) not null,
    DESCRIPTION CHARACTER VARYING(500),
    AVAILABLE   BOOLEAN default FALSE  not null,
    RENT_TIMES  BIGINT  default 0      not null,
    OWNER_ID    BIGINT                 not null,
    constraint ITEMS_PK
        primary key (ITEM_ID),
    constraint ITEMS_USERS_USER_ID_FK
        foreign key (OWNER_ID) references USERS
            on update cascade on delete cascade
);

create table if not exists BOOKINGS
(
    BOOKING_ID BIGINT auto_increment,
    ITEM_ID    BIGINT                              not null,
    OWNER_ID   BIGINT                              not null,
    FROM_DATE  DATE                                not null,
    TILL_DATE  DATE                                not null,
    STATUS     CHARACTER VARYING default 'WAITING' not null,
    USER_ID    BIGINT                              not null,
    constraint BOOKINGS_PK
        primary key (BOOKING_ID),
    constraint BOOKINGS_ITEMS_ITEM_ID_FK
        foreign key (ITEM_ID) references ITEMS
            on update cascade on delete cascade,
    constraint BOOKINGS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade,
    constraint BOOKINGS_USERS_USER_ID_FK_2
        foreign key (OWNER_ID) references USERS
            on update cascade on delete cascade
);