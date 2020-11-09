create table users
(
    id int identity (1,1) primary key,
    hash_password nvarchar(255),
    first_name nvarchar(50),
    last_name nvarchar(50),
    birthday date,
    email nvarchar(50),
    roles nvarchar(50),
    photoUri nvarchar(255),
    country nvarchar(50),
    gender nvarchar(20)
);

create table categories
(
    id int identity (1,1) primary key,
    name nvarchar(50),
    description nvarchar(50)
);

create table artists
(
    id int identity (1,1) primary key,
    user_id int,
    name nvarchar(50),
    description nvarchar(255),
    foreign key (user_id) references users (id)
);

create table albums
(
    id int identity (1,1) primary key,
    name nvarchar(50),
    author int,
    type nvarchar(50),
    description nvarchar(255),
    photo nvarchar(255),
    foreign key (author) references artists (id)
);

create table tracks
(
    id int identity (1,1) primary key,
    name nvarchar(50),
    author int,
    category_id int,
    type nvarchar(20),
    album_id int,
    description nvarchar(255),
    photo nvarchar(255),
    foreign key (author) references artists (id),
    foreign key (album_id) references albums (id),
    foreign key (category_id) references categories (id)
)



