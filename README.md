create database demo charset utf8mb4;
create table demo.user
(
    id   int primary key,
    name varchar(20)
);

insert into demo.user
values (1, '1'),
       (2, '2');

select *
from demo.user;