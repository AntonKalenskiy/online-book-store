ALTER TABLE categories AUTO_INCREMENT = 1;
INSERT INTO books (title, author, isbn, price, description, cover_image) VALUES
('Kobzar', 'Taras Shevchenko', '12345678', 45.9, 'Interesting', '11223344');
--UPDATE books SET id = 1 WHERE isbn = '12345678';
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);

INSERT INTO books (title, author, isbn, price, description, cover_image) VALUES
('Zapovit', 'Taras Shevchenko', '12345679', 45.9, 'Interesting', '11223355');
--UPDATE books SET id = 2 WHERE isbn = '12345679';
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);