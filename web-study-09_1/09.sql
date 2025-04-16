CREATE TABLE EMPLOYEES(
    id varchar2(10) not null,
    pass varchar2(10) not null,
    name varchar2(24),
    lev char(1) DEFAULT 'A', -- A : 운영자, B : 일반회원
    enter DATE DEFAULT SYSDATE, -- 등록일
    gender CHAR(1) DEFAULT '1', -- 1 : 남자, 2 : 여자
    phone varchar2(30),
    primary key(id)
);

insert into EMPLOYEES(id, pass, name, lev, gender, phone) values('pinksung', '3333', '성윤정', 'A', '2', '010-2222-2222');
insert into EMPLOYEES(id, pass, name, lev, gender, phone) values('subin', '1234', '전원지', 'B', '1', '010-9999-9999');
insert into EMPLOYEES(id, pass, name, lev, gender, phone) values('admin', '1111', '정운영', 'A', '1', '010-1111-1111');

commit;

SELECT *
from EMPLOYEES;

select id, pass, name,lev, to_char(ENTER, 'YYYY-MM-DD HH:MI:SS'),gender,phone 
from employees;