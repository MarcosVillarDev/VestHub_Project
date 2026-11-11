insert into universidades (id, nome, sigla, status) values
(1, 'Universidade de São Paulo', 'USP', 'FECHADO'),
(2, 'Universidade Estadual de Campinas', 'UNICAMP', 'FECHADO'),
(3, 'Universidade Estadual Paulista', 'UNESP', 'FECHADO');

-- A Unicamp não tem reserva só por escola pública (o benefício é o bônus PAAIS), por isso não tem EP
insert into modalidades (sigla, nome, universidade_id) values
('AC',  'Ampla Concorrência', 1),
('EP',  'Escola Pública', 1),
('PPI', 'Escola Pública - Pretos, Pardos e Indígenas', 1),
('AC',  'Ampla Concorrência (não cotistas)', 2),
('PPI', 'Cotas Étnico-Raciais', 2),
('AC',  'Sistema Universal', 3),
('EP',  'Escola Pública', 3),
('PPI', 'Escola Pública - Pretos, Pardos e Indígenas', 3);
