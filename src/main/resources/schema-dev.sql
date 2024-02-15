drop table if exists member cascade;
drop table if exists source cascade;
drop table if exists subscription cascade;
create table member (
                        creation_timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
                        member_id bigint NOT NULL AUTO_INCREMENT,
                        update_timestamp timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        email varchar(255) unique NOT NULL,
                        name varchar(255) NOT NULL,
                        primary key (member_id)
);
create table source (
                        creation_timestamp timestamp default CURRENT_TIMESTAMP,
                        source_id bigint not null auto_increment,
                        update_timestamp timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
                        description varchar(255),
                        name varchar(255),
                        url varchar(255),
                        primary key (source_id)
);
create table subscription (
                      creation_timestamp timestamp default CURRENT_TIMESTAMP,
                      member_id bigint NOT NULL,
                      source_id bigint NOT NULL,
                      subscription_id bigint not null auto_increment,
                      primary key (subscription_id)
);