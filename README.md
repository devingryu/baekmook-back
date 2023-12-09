# Baekmook (Server)
2023 Fall KWEB 준회원 대체 과제

## 목차
1. [Setup](#setup)
   - [설정 가능한 환경 변수](#설정-가능한-환경-변수)
   - [docker-compose.yml](#docker-compseyml)
   - [DB 설정](#db-설정)
2. [개발 환경 변수](#개발-환경-변수)
## Setup
### 설정 가능한 환경 변수

* **MYSQL_USER**: MySQL 유저 id (필수)
* **MYSQL_PASSWORD**: MySQL 유저 비밀번호 (필수)
* **JWT_SECRET**: JWT Secret으로 사용될 문자, openssl rand -base64 32로 생성 (필수)

* MASTER_USERNAME: 서비스 내 마스터 계정 이메일 (master)
* MASTER_PASSWORD: 서비스 내 마스터 계정 비밀번호 (masterp@ssword!)
* ACCESS_TOKEN_EXPIRY_MS: 액세스 토큰 유효기간 (1800000)
* MYSQL_URL: MySQL 주소 (mysql:3306)
* MYSQL_DATABASE: MySQL DB 이름 (SPRING)

### docker-compse.yml
```yaml
version: "3"

volumes:
  baekmook-db:

services:
  spring:
    build:
      context: https://github.com/devingryu/baekmook-back.git#06a4f1e5f83d3603bbf6158b8ece39be0d6e6d37
      dockerfile: Dockerfile
    container_name: baekmook-server
    environment:
      MYSQL_USER: [1]
      MYSQL_PASSWORD: [2]
      JWT_SECRET: [openssl rand -base64 32]
      MASTER_USERNAME: [3]
      MASTER_PASSWORD: [4]
    restart: unless-stopped
    ports:
      - "1557:8080"
    depends_on:
      - mysql
  mysql:
    image: "mysql:8.2.0"
    logging:
      driver: none
    restart: unless-stopped
    container_name: baekmook-db
    volumes:
      - baekmook-db:/var/lib/mysql
    ports:
      - "1558:3306"
    environment:
      MYSQL_DATABASE: SPRING
      MYSQL_USER: [1]
      MYSQL_PASSWORD: [2]
      MYSQL_ROOT_PASSWORD: [5]
```
환경 변수는 꼭 변경하고 사용하세요!

### DB 설정
```mysql
use SPRING;

create table authority
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null,
    constraint UK_jdeu5vgpb8k5ptsqhrvamuad2
        unique (name)
);

create table lecture
(
    is_public   bit          not null,
    id          bigint auto_increment
        primary key,
    description varchar(255) null,
    name        varchar(255) not null
);

create table user
(
    enabled      bit          not null,
    created_date datetime(6)  null,
    id           bigint auto_increment
        primary key,
    email        varchar(255) not null,
    name         varchar(255) not null,
    password     varchar(255) not null,
    student_id   varchar(255) not null,
    constraint UK_i1pkkkteed13wt581o8vanlx8
        unique (student_id),
    constraint UK_ob8kqyqqgmefl0aco34akdtpe
        unique (email)
);

create table post
(
    id           bigint auto_increment
        primary key,
    lecture_id   bigint       null,
    user_id      bigint       null,
    content      longtext     not null,
    title        varchar(255) not null,
    created_date datetime(6)  null,
    constraint FK72mt33dhhs48hf9gcqrq4fxte
        foreign key (user_id) references user (id),
    constraint FKkqnf50uycpxpw5hogkjh6hh02
        foreign key (lecture_id) references lecture (id)
);

create table lecture_user
(
    lecture_id       bigint not null,
    user_id          bigint not null,
    is_user_lecturer bit    null,
    primary key (lecture_id, user_id),
    constraint FKef19ifqyl21skxr05pxvyrg9n
        foreign key (user_id) references user (id),
    constraint FKr62qnmds9nt2ejaxk3lmhc6m1
        foreign key (lecture_id) references lecture (id)
);

create table user_authority
(
    authority_id bigint null,
    id           bigint auto_increment
        primary key,
    user_id      bigint null,
    constraint FKgvxjs381k6f48d5d2yi11uh89
        foreign key (authority_id) references authority (id),
    constraint FKpqlsjpkybgos9w2svcri7j8xy
        foreign key (user_id) references user (id)
);
```
constraint 이름은 자동 생성된 것으로, 반드시 같아야 할 필요는 없습니다.
## 개발 환경 변수
src/main/resources/application-secret.yml
```yaml
spring:
  datasource:
    username: 
    password: 

baekmook:
  # openssl rand -hex 64
  jwt:
    secret: 
    access-time-ms: 

  master:
    username: 
    password: 

---

spring:
  config:
    activate:
      on-profile: "secret-local"

  datasource:
    username: 
    password: 

baekmook:
  jwt:
    secret: 
    access-time-ms: 

  master:
    username: 
    password: 

---

spring:
  config:
    activate:
      on-profile: "secret-dev"

  datasource:
    username: 
    password: 

baekmook:
  jwt:
    secret: 
    access-time-ms: 

  master:
    username: 
    password: 

---

spring:
  config:
    activate:
      on-profile: "secret-test"

  datasource:
    username: 
    password: 

baekmook:
  jwt:
    secret: 
    access-time-ms: 

  master:
    username: 
    password: 
```