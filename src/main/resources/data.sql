INSERT INTO category (id, name) VALUES (1, 'Pottery');
INSERT INTO category (id, name) VALUES (2, 'Textiles');

INSERT INTO artist (id, name, bio) VALUES (1, 'Anya Sharma', 'Ceramicist inspired by nature.');
INSERT INTO artist (id, name, bio) VALUES (2, 'Benji K.', 'Weaving traditional patterns with modern twist.');

INSERT INTO product (id, name, description, price, artist_id, category_id) VALUES (1, 'Blue Glaze Mug', 'Hand-thrown stoneware mug.', 25.00, 1, 1);
INSERT INTO product (id, name, description, price, artist_id, category_id) VALUES (2, 'Woven Wall Hanging', 'Wool and cotton tapestry.', 120.00, 2, 2);

INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (1, 'Capacity', '300ml');
INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (1, 'Dishwasher Safe', 'Yes');
INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (2, 'Height', '50cm');
INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (2, 'Width', '30cm');
