create table if not exists member_detail
(
    id             bigint unsigned not null primary key auto_increment comment '회원 상세 정보 ID',
    profile        varchar(15)     not null comment '프로필 주소',
    introduction   TEXT            not null comment '자기소개',
    registered_at  datetime        not null comment '회원 등록 시간',
    activated_at   datetime        null comment '활성화 시간',
    deactivated_at datetime        null comment '탈퇴 시간',

    UNIQUE KEY uk_member_detail_profile (profile)
) comment '회원 상세 테이블';

create table if not exists member
(
    id               bigint unsigned not null primary key auto_increment comment '회원 ID',
    email            varchar(150)    not null comment '이메일 주소',
    nickname         varchar(100)    not null comment '닉네임',
    password_hash    varchar(200)    not null comment '비밀번호 (해시값)',
    status           varchar(50)     not null default 'PENDING' comment '회원 상태',
    member_detail_id bigint unsigned null comment '회원 상세 정보 ID',

    UNIQUE KEY uk_member_email (email),
    UNIQUE KEY uk_member_member_detail_id (member_detail_id),
    CONSTRAINT fk_member_member_detail_id FOREIGN KEY (member_detail_id) REFERENCES member_detail (id)
) comment '회원 테이블';

