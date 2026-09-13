create table magic_link_token (
    id bigint not null,
    created_at timestamp(6) with time zone not null,
    updated_at timestamp(6) with time zone not null,
    expires_at timestamp(6) with time zone not null,
    token varchar(255) not null,
    used boolean,
    user_id bigint,
    primary key (id)
);

create index idx_magic_link_token_token on magic_link_token (token);

alter table
    if exists magic_link_token drop constraint if exists uk_magic_link_token;

alter table
    if exists magic_link_token
add
    constraint uk_magic_link_token unique (token);

alter table
    if exists patients drop constraint if exists uk_patient_email;

alter table
    if exists patients
add
    constraint uk_patient_email unique (email) create sequence magic_link_token_seq start with 1 increment by 50;

alter table
    if exists magic_link_token
add
    constraint fk_magic_link_token_user foreign key (user_id) references users;