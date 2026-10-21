create table usuarios (
    id bigint not null auto_increment primary key,
    nome varchar(120) not null,
    email varchar(150) not null unique,
    senha char(60) not null
);