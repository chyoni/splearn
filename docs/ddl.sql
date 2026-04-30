create table if not exists member
(
    id            bigint unsigned not null primary key auto_increment comment '회원 ID',
    email         varchar(150)    not null comment '이메일 주소',
    nickname      varchar(100)    not null comment '닉네임',
    password_hash varchar(200)    not null comment '비밀번호 (해시값)',
    status        varchar(50)     not null default 'PENDING' comment '회원 상태',

    UNIQUE KEY uk_member_email (email)
) comment '회원 테이블';