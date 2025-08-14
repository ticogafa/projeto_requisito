create database if not exists aerocenter;

use aerocenter;

create table if not exists aviao (
    id serial primary key,
    modelo varchar(100) not null,
    fabricante varchar(100) not null,
    ano integer not null,
    capacidade integer not null
);