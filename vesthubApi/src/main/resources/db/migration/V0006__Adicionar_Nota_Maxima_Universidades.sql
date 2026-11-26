alter table universidades add column nota_maxima decimal(6,2) not null default 0;

update universidades set nota_maxima = 90 where sigla = 'USP';
update universidades set nota_maxima = 72 where sigla = 'UNICAMP';
update universidades set nota_maxima = 90 where sigla = 'UNESP';