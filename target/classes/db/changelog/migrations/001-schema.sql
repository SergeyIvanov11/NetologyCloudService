CREATE TYPE role AS ENUM ('ROLE_USER', 'ROLE_ADMIN');

CREATE TABLE if not exists public.Users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    birthdate DATE,
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    avatar BYTEA,
    role role NOT NULL
    );

CREATE TABLE if not exists public.Files (
    id BIGSERIAL PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    owner BIGSERIAL REFERENCES public.Users (id) ON DELETE RESTRICT,
    filepath TEXT NOT NULL,
    date_of_upload TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
