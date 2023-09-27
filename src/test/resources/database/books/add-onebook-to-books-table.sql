ALTER TABLE categories AUTO_INCREMENT = 1;
INSERT INTO books (title, author, isbn, price, description, cover_image) VALUES
('Kobzar', 'Taras Shevchenko', '12345678', 45.9, 'Interesting', '11223344');
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);