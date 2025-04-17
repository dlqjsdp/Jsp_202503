create table member(
    name varchar2(20) ,
    userid varchar2(10) ,
    pwd varchar2(10),
    email varchar2(20),
    phone char(13),  -- 010-3380-6423
    admin number(1) default 0, -- 사용자: 0, 관리자:1
    primary key(userid)
);

insert into member values('이소미', 'somi', '1234', 'gmd@naver.com','010-2362-5157', 0);
insert into member values('하상오', 'sang12', '1234', 'ha12@naver.com','010-5926-8888', 1);
insert into member values('김윤승', 'light', '1234', 'youn1004@naver.com','010-9999-8282', 0);

commit;

CREATE TABLE EMPLOYEE(
	name varchar2(20),
	address varchar2(100),
	ssn varchar2(15)
);

insert into EMPLOYEE VALUES('duke', 'seoul', '970224-1039234');
insert into EMPLOYEE VALUES('pororo', 'pusan', '001222-1038782');
insert into EMPLOYEE VALUES('candy', 'deajeon', '981221-1829192');

commit;

select *
from EMPLOYEE;

CREATE TABLE item(
NAME VARCHAR(20),
price NUMBER(8),
description VARCHAR2(100));

CREATE TABLE item(
	name varchar2(20),
	price number(8),
	description varchar2(100)
);

commit;