create sequence hibernate_sequence start with 1 increment by 1

create table album_likes
(
    album_id int not null,
    user_id  int not null,
    primary key (album_id, user_id)
)

create table albums
(
    id                 int not null,
    created_by         nvarchar(255),
    created_date       datetime2,
    deleted            bit,
    last_modified_by   nvarchar(255),
    last_modified_date datetime2,
    version            int,
    description        nvarchar(255),
    name               nvarchar(255),
    photo_uri          nvarchar(255),
    release_date       datetime2,
    type               nvarchar(255),
    primary key (id)
)

create table artist_album
(
    album_id   int not null,
    artists_id int not null,
    primary key (album_id, artists_id)
)

create table artist_likes
(
    artist_id int not null,
    user_id   int not null,
    primary key (artist_id, user_id)
)

create table artists
(
    id                 int          not null,
    created_by         nvarchar(255),
    created_date       datetime2,
    deleted            bit,
    last_modified_by   nvarchar(255),
    last_modified_date datetime2,
    version            int,
    description        nvarchar(255) not null,
    name               nvarchar(255) not null,
    primary key (id)
)

create table atrist_track
(
    track_id  int not null,
    artist_id int not null,
    primary key (track_id, artist_id)
)

create table categories_likes
(
    category_id int not null,
    user_id     int not null,
    primary key (category_id, user_id)
)

create table category_track
(
    track_id    int not null,
    category_id int not null,
    primary key (track_id, category_id)
)

create table track_categories
(
    id                 int not null,
    created_by         nvarchar(255),
    created_date       datetime2,
    deleted            bit,
    last_modified_by   nvarchar(255),
    last_modified_date datetime2,
    version            int,
    description        nvarchar(255),
    name               nvarchar(255),
    primary key (id)
)

create table track_likes
(
    track_id int not null,
    user_id  int not null,
    primary key (track_id, user_id)
)

create table tracks
(
    id                 int not null,
    created_by         nvarchar(255),
    created_date       datetime2,
    deleted            bit,
    last_modified_by   nvarchar(255),
    last_modified_date datetime2,
    version            int,
    description        nvarchar(255),
    name               nvarchar(255),
    photo_uri          nvarchar(255),
    published          bit not null,
    release_date       datetime2,
    track_text         nvarchar(255),
    type               nvarchar(255),
    album_id           int,
    primary key (id)
)

create table user_role
(
    user_id int not null,
    roles   nvarchar(255)
)

create table users
(
    id                 int          not null,
    created_by         nvarchar(255),
    created_date       datetime2,
    deleted            bit,
    last_modified_by   nvarchar(255),
    last_modified_date datetime2,
    version            int,
    activation_code    nvarchar(255),
    birthday           datetime2    not null,
    country            nvarchar(255) not null,
    email              nvarchar(255) not null,
    first_name         nvarchar(255) not null,
    gender             nvarchar(255) not null,
    hash_password      nvarchar(255),
    last_name          nvarchar(255) not null,
    photo_uri          nvarchar(255),
    primary key (id)
)

alter table album_likes
    add constraint FKpldfgyhopx93mlmnvodi89qdo foreign key (user_id) references users

alter table album_likes
    add constraint FK1piao2qhdm5xxs65sux0mbxe0 foreign key (album_id) references albums

alter table artist_album
    add constraint FKn18wq2frg086v8bmxe7c9ydjr foreign key (artists_id) references artists

alter table artist_album
    add constraint FKct2a6ek3bfsc7tpnewtrojj8t foreign key (album_id) references albums

alter table artist_likes
    add constraint FKrg0exkc3eek60h95hextjbenb foreign key (user_id) references users

alter table artist_likes
    add constraint FKplppovpvfspvidxii3dxnynv6 foreign key (artist_id) references artists

alter table atrist_track
    add constraint FKll3ek2usv7e76aq9ffypdnnw4 foreign key (artist_id) references artists

alter table atrist_track
    add constraint FKiq3n2qcatwyyaeuccwramb540 foreign key (track_id) references tracks

alter table categories_likes
    add constraint FK90kr7g74vh742qaim2gsog096 foreign key (user_id) references users

alter table categories_likes
    add constraint FKp1jcwi3st95ywtu21g75bs373 foreign key (category_id) references track_categories

alter table category_track
    add constraint FKgwc5ls329dgie5a4c1up63y2l foreign key (category_id) references track_categories

alter table category_track
    add constraint FK5pd1u38l19dko2fobxpifl2sc foreign key (track_id) references tracks

alter table track_likes
    add constraint FKqiem4pevcl4py592dlqtbqd8u foreign key (user_id) references users

alter table track_likes
    add constraint FKglieae0f87ruh0ixh00uvcqqf foreign key (track_id) references tracks

alter table tracks
    add constraint FKdcmijveo7n1lql01vav1u2jd2 foreign key (album_id) references albums

alter table user_role
    add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users