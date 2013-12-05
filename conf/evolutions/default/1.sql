# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table itinerary (
  id                        bigint not null,
  trip_id                   bigint not null,
  title                     varchar(255),
  description               varchar(255),
  constraint pk_itinerary primary key (id))
;

create table linked_account (
  id                        bigint not null,
  user_id                   bigint,
  provider_user_id          varchar(255),
  provider_key              varchar(255),
  constraint pk_linked_account primary key (id))
;

create table location_point (
  id                        bigint not null,
  itinerary_id              bigint not null,
  title                     varchar(255),
  latitude                  float,
  longitude                 float,
  description               varchar(255),
  constraint pk_location_point primary key (id))
;

create table region (
  id                        bigint not null,
  long_name                 varchar(255),
  short_name                varchar(255),
  uri                       varchar(255),
  region_type               integer,
  parent_id                 bigint,
  constraint ck_region_region_type check (region_type in (0,1,2,3)),
  constraint pk_region primary key (id))
;

create table security_role (
  id                        bigint not null,
  role_name                 varchar(255),
  constraint pk_security_role primary key (id))
;

create table token_action (
  id                        bigint not null,
  token                     varchar(255),
  target_user_id            bigint,
  type                      varchar(2),
  created                   timestamp,
  expires                   timestamp,
  constraint ck_token_action_type check (type in ('EV','PR')),
  constraint uq_token_action_token unique (token),
  constraint pk_token_action primary key (id))
;

create table trip (
  id                        bigint not null,
  author_id                 bigint,
  uri                       varchar(255),
  title                     varchar(255),
  description               varchar(255),
  request_publish_date      timestamp,
  published_date            timestamp,
  region_id                 bigint,
  constraint pk_trip primary key (id))
;

create table users (
  id                        bigint not null,
  email                     varchar(255),
  name                      varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  last_login                timestamp,
  active                    boolean,
  email_validated           boolean,
  constraint pk_users primary key (id))
;

create table user_permission (
  id                        bigint not null,
  value                     varchar(255),
  constraint pk_user_permission primary key (id))
;


create table users_security_role (
  users_id                       bigint not null,
  security_role_id               bigint not null,
  constraint pk_users_security_role primary key (users_id, security_role_id))
;

create table users_user_permission (
  users_id                       bigint not null,
  user_permission_id             bigint not null,
  constraint pk_users_user_permission primary key (users_id, user_permission_id))
;
create sequence itinerary_seq;

create sequence linked_account_seq;

create sequence location_point_seq;

create sequence region_seq;

create sequence security_role_seq;

create sequence token_action_seq;

create sequence trip_seq;

create sequence users_seq;

create sequence user_permission_seq;

alter table itinerary add constraint fk_itinerary_trip_1 foreign key (trip_id) references trip (id) on delete restrict on update restrict;
create index ix_itinerary_trip_1 on itinerary (trip_id);
alter table linked_account add constraint fk_linked_account_user_2 foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_linked_account_user_2 on linked_account (user_id);
alter table location_point add constraint fk_location_point_itinerary_3 foreign key (itinerary_id) references itinerary (id) on delete restrict on update restrict;
create index ix_location_point_itinerary_3 on location_point (itinerary_id);
alter table region add constraint fk_region_parent_4 foreign key (parent_id) references region (id) on delete restrict on update restrict;
create index ix_region_parent_4 on region (parent_id);
alter table token_action add constraint fk_token_action_targetUser_5 foreign key (target_user_id) references users (id) on delete restrict on update restrict;
create index ix_token_action_targetUser_5 on token_action (target_user_id);
alter table trip add constraint fk_trip_author_6 foreign key (author_id) references users (id) on delete restrict on update restrict;
create index ix_trip_author_6 on trip (author_id);
alter table trip add constraint fk_trip_region_7 foreign key (region_id) references region (id) on delete restrict on update restrict;
create index ix_trip_region_7 on trip (region_id);



alter table users_security_role add constraint fk_users_security_role_users_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_security_role add constraint fk_users_security_role_securi_02 foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;

alter table users_user_permission add constraint fk_users_user_permission_user_01 foreign key (users_id) references users (id) on delete restrict on update restrict;

alter table users_user_permission add constraint fk_users_user_permission_user_02 foreign key (user_permission_id) references user_permission (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists itinerary;

drop table if exists linked_account;

drop table if exists location_point;

drop table if exists region;

drop table if exists security_role;

drop table if exists token_action;

drop table if exists trip;

drop table if exists users;

drop table if exists users_security_role;

drop table if exists users_user_permission;

drop table if exists user_permission;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists itinerary_seq;

drop sequence if exists linked_account_seq;

drop sequence if exists location_point_seq;

drop sequence if exists region_seq;

drop sequence if exists security_role_seq;

drop sequence if exists token_action_seq;

drop sequence if exists trip_seq;

drop sequence if exists users_seq;

drop sequence if exists user_permission_seq;

