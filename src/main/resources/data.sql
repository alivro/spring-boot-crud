INSERT INTO author (author_id, first_name, middle_name, last_name, pseudonym) VALUES
  (1, 'Eric', 'Arthur', 'Blair', 'George Orwell'),
  (2, 'Herbert', 'George', 'Wells', 'H. G. Wells'),
  (3, 'Aldous', 'Leonard', 'Huxley', 'Aldous Huxley'),
  (4, 'Charles', 'Lutwidge', 'Dodgson' ,'Lewis Carroll'),
  (5, 'Brian', 'Wilson', 'Kernighan' ,'Brian W. Kernighan'),
  (6, 'Dennis', 'MacAlistair', 'Ritchie' ,'Dennis M. Ritchie'),
  (7, 'Paul', 'J.', 'Deitel' ,'Paul Deitel'),
  (8, 'Harvey', 'M.', 'Deitel' ,'Harvey Deitel'),
  (9, 'Abraham', null, 'Silberschatz' ,'Abraham Silberschatz'),
  (10, 'Peter', 'Baer', 'Galvin' ,'Peter B. Galvin'),
  (11, 'Greg', null, 'Gagne' ,'Greg Gagne'),
  (12, 'Alice', null, 'Zhao' ,'Alice Zhao');

INSERT INTO book (book_id, title, subtitle, total_pages, publisher, published_date, isbn_13, isbn_10) VALUES
  (1, '1984', '75th Anniversary', 384, 'Berkley', '2003-05-06', '9780452284234', null),
  (2, 'Animal Farm', '75th Anniversary', 128, 'Berkley', '2003-05-06', '9780452284241', null),
  (3, 'The Invisible Man', 'A Grotesque Romance', 208, 'Melville House', '2014-07-29', '9781612193236', null),
  (4, 'The Time Machine', null, 128, 'Penguin Classics', '2005-05-31', '9780141439976', null),
  (5, 'The War of the Worlds', null, 224, 'Signet', '2007-09-04', '9780451530653', null),
  (6, 'Brave New World', null, 272, 'Harper', '2017-05-09', '9780062696120', '0062696122'),
  (7, 'Alice''s Adventures in Wonderland', null, 64, 'Penguin', '2023-02-02', '9780241588864', null),
  (8, 'Alice Through the Looking Glass', null, 80, 'Penguin', '2024-02-08', '9780241636763', null),
  (9, 'The C programming Language', null, 288, 'Pearson', '1988-03-22', '9780131103627', '0131103628'),
  (10, 'Java How To Program', null, 1596, 'Prentice Hall', '2006-12-18', '9780132222204', null),
  (11, 'Operating System Concepts', null, 921, 'John Wiley & Sons Inc', '2004-12-14', '9780471694663', '0471694665');

INSERT INTO book_author (book_id, author_id) VALUES
  (1, 1),
  (2, 1),
  (3, 2),
  (4, 2),
  (5, 2),
  (6, 3),
  (7, 4),
  (8, 4),
  (9, 5),
  (9, 6),
  (10, 7),
  (10, 8),
  (11, 9),
  (11, 10),
  (11, 11);