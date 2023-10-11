ALTER TABLE cart_items AUTO_INCREMENT = 1;
INSERT INTO cart_items (shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1);

INSERT INTO shopping_carts_cart_items (shopping_cart_id, cart_item_id)
VALUES (1, 1);