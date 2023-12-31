create table user(
    user_id bigint not null auto_increment,
    email varchar(100) not null,
    primary key (user_id)
);

create table source (
    source_id bigint not null auto_increment,
    source_name varchar(100) not null,
    description varchar(100) not null,
    url varchar(255) not null,
    primary key (source_id)
);


create table subscription (
  user_id bigint not null auto_increment,
  source_id bigint not null,
  primary key(user_id, source_id),
  key user_idx (user_id),
  key source_idx (source_id)
);
