CREATE SEQUENCE author_seq
  START WITH 101
  INCREMENT BY 1;

CREATE SEQUENCE book_seq
  START WITH 1001
  INCREMENT BY 1;

CREATE TABLE author (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100) NULL,
    last_name VARCHAR(100) NOT NULL,
    pseudonym VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE book (
    id BIGINT PRIMARY KEY,
    title VARCHAR (255) NOT NULL,
    subtitle VARCHAR (255) NULL,
    author VARCHAR (100) NOT NULL,
    total_pages INT NOT NULL,
    publisher VARCHAR (50) NOT NULL,
    published_date DATE NOT NULL,
    isbn_13 VARCHAR (13) NOT NULL,
    isbn_10 VARCHAR (10) NULL
);
