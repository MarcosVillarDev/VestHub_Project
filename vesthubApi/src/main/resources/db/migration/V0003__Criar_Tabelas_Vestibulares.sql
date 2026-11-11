create table universidades (
    id bigint not null auto_increment primary key,
    nome varchar(120) not null,
    sigla varchar(10) not null unique,
    status varchar(30) not null
);

create table modalidades (
    id bigint not null auto_increment primary key,
    sigla varchar(10) not null,
    nome varchar(120) not null,
    universidade_id bigint not null,
    unique (universidade_id, sigla)
);

create table cursos (
    id bigint not null auto_increment primary key,
    nome varchar(150) not null,
    campus varchar(120) not null,
    turno varchar(15) not null,
    universidade_id bigint not null
);

create table notas_corte (
    id bigint not null auto_increment primary key,
    nota_corte decimal(6,2) not null,
    curso_id bigint not null,
    modalidade_id bigint not null,
    unique (curso_id, modalidade_id)
);

create table universidades_favoritas (
    id bigint not null auto_increment primary key,
    usuario_id bigint not null,
    universidade_id bigint not null,
    unique (usuario_id, universidade_id)
);

create table desempenhos (
    id bigint not null auto_increment primary key,
    nota decimal(6,2) not null,
    usuario_id bigint not null,
    universidade_id bigint not null,
    modalidade_id bigint not null,
    unique (usuario_id, universidade_id)
);

alter table modalidades add constraint fk_modalidades_universidades
foreign key (universidade_id) references universidades(id);

alter table cursos add constraint fk_cursos_universidades
foreign key (universidade_id) references universidades(id);

alter table notas_corte add constraint fk_notas_corte_cursos
foreign key (curso_id) references cursos(id);

alter table notas_corte add constraint fk_notas_corte_modalidades
foreign key (modalidade_id) references modalidades(id);

alter table universidades_favoritas add constraint fk_favoritas_usuarios
foreign key (usuario_id) references usuarios(id) on delete cascade;

alter table universidades_favoritas add constraint fk_favoritas_universidades
foreign key (universidade_id) references universidades(id);

alter table desempenhos add constraint fk_desempenhos_usuarios
foreign key (usuario_id) references usuarios(id) on delete cascade;

alter table desempenhos add constraint fk_desempenhos_universidades
foreign key (universidade_id) references universidades(id);

alter table desempenhos add constraint fk_desempenhos_modalidades
foreign key (modalidade_id) references modalidades(id);
