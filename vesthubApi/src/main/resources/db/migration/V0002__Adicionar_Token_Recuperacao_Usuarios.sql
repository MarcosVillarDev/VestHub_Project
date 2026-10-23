alter table usuarios
    add column token_recuperacao varchar(36),
    add column token_expiracao datetime;