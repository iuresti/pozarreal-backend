CREATE TABLE professors
(
    id      varchar(40) primary key,
    name    varchar(200) not null,
    phone   varchar(20)  not null,
    address varchar(200) null
);

CREATE TABLE courses
(
    id           varchar(40) primary key,
    name         varchar(100) not null unique,
    professor_id varchar(40)  not null references professors
);

CREATE TABLE schedule_by_course
(
    id         varchar(40) primary key,
    course_id  varchar(40) references courses,
    weekday    varchar(40) not null, -- MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    start_time int         not null, -- 1300 = 13:00, 730 = 7:30
    end_time   int         not null  -- 1300 = 13:00, 730 = 7:30
);

CREATE TABLE course_assistant
(
    id                 varchar(40) primary key,
    name               varchar(200),
    course_id          varchar(40) references courses,
    birth_date         date,
    phone              varchar(20),
    responsible_name   varchar(200),
    responsible_phone  varchar(20),
    responsible_name2  varchar(200),
    responsible_phone2 varchar(20),
    email              varchar(200),
    notes              varchar(1024)
);

CREATE TABLE course_payment
(
    id                  varchar(40) primary key,
    payment_date        date,
    user_id             varchar(40) references users,
    course_assistant_id varchar(40) references course_assistant,
    concept             varchar(200),
    amount              decimal(10, 2),
    receipt_number      varchar,
    notes               varchar(500)
);
