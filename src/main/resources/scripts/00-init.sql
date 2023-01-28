create table "client"
(
    "id"        bigserial primary key,
    "user_name" text unique not null,
    "password"  text        not null,
    "user_role" text        not null
);

create table "article"
(
    "id"           bigserial primary key,
    "author_id"    bigint not null,
    "title"        text   not null,
    "content"      text   not null,
    "published_at" timestamp without time zone not null,
    "created_on"   date
);

ALTER TABLE "article"
    ADD FOREIGN KEY ("author_id") REFERENCES "client" ("id");

insert into client (user_name, password, user_role)
values ('admin', '$2a$10$8Od584Z2Vtb95Z.xwQdmb.jJAEQlNBiLIDDFKtB40AzbhqTBnF6Fe', 'ADMIN');