create table templates
(
    id          serial primary key,
    name        varchar(255) not null unique,
    type        varchar(50)  not null,
    description text,
    content     text         not null
);
