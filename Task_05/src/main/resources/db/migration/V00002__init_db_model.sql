insert into users (name) values ('Jo');
insert into users (name) values ('Bob');

insert into products (user_id, account_number, balance, type)
select id, '40817810000000000001', 1, 'ACCOUNT' from users where name='Jo';

insert into products (user_id, account_number, balance, type)
select id, '40817810000000000002', 2, 'CARD' from users where name='Jo';

insert into products (user_id, account_number, balance, type)
select id, '40817810000000000003', 3, 'ACCOUNT' from users where name='Bob';