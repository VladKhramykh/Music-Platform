 create sequence hibernate_sequence start with 1 increment by 1

create table albums
(
    id                 int not null,
    created_by         varchar(255),
    created_date       datetime2,
    deleted            bit,
    last_modified_by   varchar(255),
    last_modified_date datetime2,
    version            int,
    description        varchar(255),
    name               varchar(255),
    photo_uri          varchar(255),
    release_date       datetime2,
    type               varchar(255),
    primary key (id)
)

create table artist_album
(
    album_id   int not null,
    artists_id int not null,
    primary key (album_id, artists_id)
)

create table artists
(
    id                 int          not null,
    created_by         varchar(255),
    created_date       datetime2,
    deleted            bit,
    last_modified_by   varchar(255),
    last_modified_date datetime2,
    version            int,
    description        varchar(255) not null,
    name               varchar(255) not null,
    primary key (id)
)

create table atrist_track
(
    track_id   int not null,
    artists_id int not null,
    primary key (track_id, artists_id)
)

create table category_track
(
    track_id      int not null,
    categories_id int not null,
    primary key (track_id, categories_id)
)

create table track_categories
(
    id                 int not null,
    created_by         varchar(255),
    created_date       datetime2,
    deleted            bit,
    last_modified_by   varchar(255),
    last_modified_date datetime2,
    version            int,
    description        varchar(255),
    name               varchar(255),
    primary key (id)
)

create table tracks
(
    id                 int not null,
    created_by         varchar(255),
    created_date       datetime2,
    deleted            bit,
    last_modified_by   varchar(255),
    last_modified_date datetime2,
    version            int,
    description        varchar(255),
    name               varchar(255),
    photo_uri          varchar(255),
    published          bit not null,
    release_date       datetime2,
    track_text         varchar(255),
    type               varchar(255),
    album_id           int,
    primary key (id)
)

create table user_role
(
    user_id int not null,
    roles   varchar(255)
)

create table users
(
    id                   int          not null,
    created_by           varchar(255),
    created_date         datetime2,
    deleted              bit,
    last_modified_by     varchar(255),
    last_modified_date   datetime2,
    version              int,
    activation_code      varchar(255),
    birthday             datetime2    not null,
    country              varchar(255) not null,
    date_of_registration datetime2,
    email                varchar(255) not null,
    first_name           varchar(255) not null,
    gender               varchar(255) not null,
    hash_password        varchar(255),
    last_name            varchar(255) not null,
    photo_uri            varchar(255),
    primary key (id)
)

alter table artist_album
    add constraint FKn18wq2frg086v8bmxe7c9ydjr foreign key (artists_id) references artists

alter table artist_album
    add constraint FKct2a6ek3bfsc7tpnewtrojj8t foreign key (album_id) references albums

alter table atrist_track
    add constraint FKkhawpro90sh3osr7wcucw8970 foreign key (artists_id) references artists

alter table atrist_track
    add constraint FKiq3n2qcatwyyaeuccwramb540 foreign key (track_id) references tracks

alter table category_track
    add constraint FKetb1kxlh35akjia46q802dbde foreign key (categories_id) references track_categories

alter table category_track
    add constraint FK5pd1u38l19dko2fobxpifl2sc foreign key (track_id) references tracks

alter table tracks
    add constraint FKdcmijveo7n1lql01vav1u2jd2 foreign key (album_id) references albums

alter table user_role
    add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users