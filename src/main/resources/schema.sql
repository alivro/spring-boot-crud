CREATE SEQUENCE book_seq
  START WITH 1001
  INCREMENT BY 1;

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

