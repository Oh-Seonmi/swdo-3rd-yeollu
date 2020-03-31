drop sequence seq_searched_stores;
drop table searched_stores;
drop table insta_locations;
drop table users;
drop table stores;
drop table mango_stores;
drop table follows;
drop table likes;
<<<<<<< HEAD
drop table insta_replys;
=======
<<<<<<< HEAD
=======
<<<<<<< HEAD
drop table insta_replys;
=======
>>>>>>> 301ac4fe77715f3060bd198e1e4f15375b072219
>>>>>>> 1b4a8406c316d27b14b2f16962ddb35195310137
>>>>>>> 6d621ec112e746f6b5abe4dfda2ab8b67f7cda38

create table users(
    user_email              varchar2(50)            primary key
    , user_pw               varchar2(200)           not null
    , user_name             varchar2(30)            not null
    , user_type             varchar2(10)
    , user_profile          varchar2(1000)
);

-- sqlldr userid=hr/hr control='C:\Users\user\Desktop\stores3_control.ctl'
-- sqlplus sys as sysdba
-- alter system set processes=500 scope=spfile;
-- shutdown immediate
-- startup
-- show parameter processes
create table stores(
    store_no                varchar2(200)           primary key
    , store_name            varchar2(200)
    , store_name2           varchar2(200)
    , store_cate1           varchar2(200)
    , store_cate2           varchar2(200)
    , store_adr             varchar2(300)
    , store_adr1            varchar2(300)
    , store_adr2            varchar2(300)
    , store_x               number
    , store_y               number
);

create table insta_locations (
    location_id             varchar2(200)           primary key
    , store_no              varchar2(200)
    , location_indate       date                    default sysdate
    , constraint fk1_insta_locations foreign key (store_no) references stores(store_no)
);

create table searched_stores (
    searched_no             number                  primary key
    , store_name            varchar2(200)
    , searched_indate       date                    default sysdate
);

create sequence seq_searched_stores;

-- sqlldr userid=hr/hr control='C:\Users\user\Desktop\mango_stores_control.ctl'
create table mango_stores (
    store_no                varchar2(200)           primary key
    , mango_tel             varchar2(100)
    , mango_kind            varchar2(100)
    , mango_price           varchar2(100)
    , mango_parking         varchar2(100)
    , mango_time            varchar2(100)
    , mango_break_time      varchar2(100)
    , mango_last_order      varchar2(100)
    , mango_holiday         varchar2(100)
    , mango_indate          date                    default sysdate
    , constraint fk1_mango_stores foreign key (store_no) references stores(store_no)
);

create table follows (
    user_email              varchar2(50)            not null
    , follows_following     varchar2(50)            not null
    , follows_indate        date                    default sysdate
    , constraint pk_follows primary key (user_email, follows_following)
    , constraint fk1_follows foreign key (user_email) references users(user_email)
);

create table likes (
    user_email 	            varchar2(50)            not null
    , store_no 		        varchar2(200)           not null
    , likes_indate          date                    default sysdate
    , constraint pk_likes primary key (user_email, store_no)
    , constraint fk1_likes foreign key (user_email) references users(user_email)
    , constraint fk2_likes foreign key (store_no) references stores(store_no)
);

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 6d621ec112e746f6b5abe4dfda2ab8b67f7cda38
create table insta_replys(
reply_no 		number 	primary key 
,store_no 	varchar2(200)
,user_email	 varchar2(50)
,reply_contents 	varchar2(1000)
,reply_star	 number
,reply_indate	 date default sysdate
,constraint fk_insta_replys foreign key (store_no) references stores(store_no)
);

<<<<<<< HEAD
=======
=======
>>>>>>> 301ac4fe77715f3060bd198e1e4f15375b072219
>>>>>>> 1b4a8406c316d27b14b2f16962ddb35195310137
>>>>>>> 6d621ec112e746f6b5abe4dfda2ab8b67f7cda38
commit;












--------------------------------------------------------------------------------------------------------------------------------
select * from users;
select * from stores order by store_no asc;
select count(*) from stores;
select * from insta_locations;
select count(*) from insta_locations;
select * from searched_stores;
select * from mango_stores order by store_no asc;
select count(*) from mango_stores; 
--------------------------------------------------------------------------------------------------------------------------------
select
    s.store_no
    , s.store_name
    , m.mango_tel
    , m.mango_kind
    , m.mango_price
    , m.mango_parking
    , m.mango_time
    , m.mango_break_time
    , m.mango_last_order
    , m.mango_holiday
from
    stores s, mango_stores m
where
    s.store_no = m.store_no
order by s.store_no;
--------------------------------------------------------------------------------------------------------------------------------
select
	store_no
	, store_name
	, store_name2
	, store_cate1
	, store_cate2
	, store_adr
	, store_adr1
	, store_adr2
	, store_x
	, store_y
from
	stores
order by
	store_no asc;
--------------------------------------------------------------------------------------------------------------------------------
select
    s.store_no
from
    insta_locations i, stores s
where
    i.store_no = s.store_no
group by
    s.store_no;
--------------------------------------------------------------------------------------------------------------------------------