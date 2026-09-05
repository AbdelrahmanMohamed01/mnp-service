create database if not exists mnp_db;
use mnp_db;

create table if not exists operators(
	id bigint auto_increment primary key,
    name varchar(45) not null unique,
    code varchar(45) not null unique,
    prefix_start varchar(15) not null ,
    prefix_end varchar(15) not null
)engine=InnoDB default charset=utf8mb4;

create table if not exists porting_requests(
	id bigint auto_increment primary key,
    phone_number varchar(15) not null,
    status varchar(20) not null,
    rejection_reason varchar(255),
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp on update current_timestamp,
    recipient_operator_id bigint not null,
    donor_operator_id bigint not null,
    
    constraint fk_recipient_operator foreign key(recipient_operator_id) references operators(id),
    constraint fk_donor_operator foreign key(donor_operator_id) references operators(id),
    
    index idx_phone_number (phone_number),
    index idx_phone_status_updated (phone_number, status, updated_at DESC),
    index idx_status_created_at (status,created_at),
    index idx_recipient_operator_id (recipient_operator_id),
    index idx_donor_operator_id (donor_operator_id)
) engine=InnoDB default charset=utf8mb4;

insert into operators(name,code,prefix_start,prefix_end) values
('Vodafone', 'vodafone', '01000000000', '01099999999'),
('Orange',   'orange',   '01200000000', '01299999999'),
('Etisalat', 'etisalat', '01100000000', '01199999999');