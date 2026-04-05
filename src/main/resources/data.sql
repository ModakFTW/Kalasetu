INSERT INTO site_user (name, email, password, role) VALUES ('Jane Collector', 'jane@collector.com', 'password', 'CUSTOMER');
INSERT INTO site_user (name, email, password, role) VALUES ('Anya Sharma', 'anya@artisan.com', 'password', 'ARTIST');
INSERT INTO site_user (name, email, password, role) VALUES ('Benji K.', 'benji@artisan.com', 'password', 'ARTIST');

INSERT INTO category (id, name) VALUES (1, 'Pottery');
INSERT INTO category (id, name) VALUES (2, 'Textiles');
INSERT INTO category (id, name) VALUES (3, 'Canvas Art');
INSERT INTO category (id, name) VALUES (4, 'Woodwork');
INSERT INTO category (id, name) VALUES (5, 'Jewelry');

INSERT INTO artist (id, name, bio, email) VALUES (1, 'Anya Sharma', 'Ceramicist inspired by nature.', 'anya@artisan.com');
INSERT INTO artist (id, name, bio, email) VALUES (2, 'Benji K.', 'Weaving traditional patterns with modern twist.', 'benji@artisan.com');
INSERT INTO artist (id, name, bio, email) VALUES (3, 'Ramesh T.', 'Passionate about vibrant mountain landscapes.', 'ramesh@artisan.com');
INSERT INTO artist (id, name, bio, email) VALUES (4, 'Sarah M.', 'Minimalist wooden sculptures and functional art.', 'sarah@artisan.com');

INSERT INTO product (id, name, description, price, image_url, image_hash, artist_id, category_id) VALUES (1, 'Blue Glaze Mug', 'Hand-thrown stoneware mug.', 25.00, '/images/blue glaze ug.jpg', null, 1, 1);
INSERT INTO product (id, name, description, price, image_url, image_hash, artist_id, category_id) VALUES (2, 'Woven Wall Hanging', 'Wool and cotton tapestry.', 120.00, '/images/woven wall hanging.jpg', null, 2, 2);
INSERT INTO product (id, name, description, price, image_url, image_hash, artist_id, category_id) VALUES (3, 'Mountain Landscape Painting', 'Original oil on canvas.', 180.00, '/images/mountain landscape painting.jpg', null, 3, 3);
INSERT INTO product (id, name, description, price, image_url, image_hash, artist_id, category_id) VALUES (4, 'Hand-Carved Walnut Bowl', 'Organic shape, food-safe finish.', 85.00, '/images/hand carved walnut bowl.jpg', null, 4, 4);
INSERT INTO product (id, name, description, price, image_url, image_hash, artist_id, category_id) VALUES (5, 'Terracotta Vase', 'Rustic clay vase for dried flowers.', 45.00, '/images/teracotta vase.jpg', null, 1, 1);
INSERT INTO product (id, name, description, price, image_url, image_hash, artist_id, category_id) VALUES (6, 'Geometric Brass Earrings', 'Hand-cut recycled brass.', 35.00, '/images/geometric brass earisgns.jpg', null, 2, 5);

INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (1, 'Capacity', '300ml');
INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (1, 'Dishwasher Safe', 'Yes');
INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (2, 'Height', '50cm');
INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (2, 'Width', '30cm');
INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (3, 'Dimensions', '16x20 inches');
INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (4, 'Material', 'Walnut Wood');
INSERT INTO product_attributes (product_id, attribute_key, attribute_value) VALUES (5, 'Waterproof', 'No, for dried arrangements only');

ALTER TABLE artist ALTER COLUMN id RESTART WITH 100;
ALTER TABLE category ALTER COLUMN id RESTART WITH 100;
ALTER TABLE product ALTER COLUMN id RESTART WITH 100;
ALTER TABLE site_user ALTER COLUMN id RESTART WITH 100;
