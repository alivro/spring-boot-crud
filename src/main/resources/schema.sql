CREATE SEQUENCE book_seq
  START WITH 1001
  INCREMENT BY 1;

CREATE TABLE book (
    id BIGINT PRIMARY KEY,
    title VARCHAR (255) NOT NULL,
    author VARCHAR (100) NOT NULL,
    total_pages INT NOT NULL,
    publisher VARCHAR (50) NOT NULL,
    published_date DATE NOT NULL,
    isbn VARCHAR (13) NOT NULL
);

