create table users
(
    id       bigserial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role     varchar(255) not null
);

create table photo
(
    id         bigserial primary key,
    photo_data bytea  not null,
    user_id    bigint not null references users (id)
);

create table template
(
    id          bigserial primary key,
    name        varchar(255) not null,
    description varchar(255) not null,
    image_data  bytea        not null
);

create table resume
(
    id          bigserial primary key,
    full_name   varchar(255)                not null,
    email       varchar(255)                not null,
    skills      varchar(255)                not null,
    objective   varchar(255)                not null,
    interests   varchar(255),
    created_at  timestamp without time zone not null,
    modified_at timestamp without time zone not null,
    user_id     bigint                      not null references users (id),
    photo_id    bigint                               references photo (id),
    template_id bigint                      not null references template (id)
);


create table education
(
    id             bigserial primary key,
    degree         varchar(255) not null,
    institution    varchar(255) not null,
    year_completed int          not null,
    resume_id      bigint references resume (id) on delete cascade
);

create table work_experience
(
    id           bigserial primary key,
    company_name varchar(255) not null,
    position     varchar(255) not null,
    start_date   date,
    end_date     date,
    resume_id    bigint references resume (id) on delete cascade
);
