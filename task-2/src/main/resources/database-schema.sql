create table if not exists students (
    id varchar(50) primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    middle_name varchar(50),
    birth_date date not null,
    student_group varchar(50) not null
);