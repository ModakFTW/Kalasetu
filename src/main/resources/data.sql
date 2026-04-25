INSERT INTO site_user (name, email, password, role) VALUES ('Jane Collector', 'jane@collector.com', 'password', 'CUSTOMER');
INSERT INTO site_user (name, email, password, role) VALUES ('Anya Sharma', 'anya@artisan.com', 'password', 'ARTIST');
INSERT INTO site_user (name, email, password, role) VALUES ('Benji K.', 'benji@artisan.com', 'password', 'ARTIST');
INSERT INTO site_user (name, email, password, role) VALUES ('Arun Verma', 'arun@artisan.com', 'password', 'ARTIST');
INSERT INTO site_user (name, email, password, role) VALUES ('Sara Martinez', 'sara@artisan.com', 'password', 'ARTIST');
INSERT INTO site_user (name, email, password, role) VALUES ('KalaSetu Admin', 'admin@kalasetu.com', 'admin123', 'ADMIN');

INSERT INTO category (id, name, name_hi, name_mr) VALUES (1, 'Pottery', 'मिट्टी के बर्तन', 'मातीची भांडी');
INSERT INTO category (id, name, name_hi, name_mr) VALUES (2, 'Textiles', 'कपड़ा', 'कापड');
INSERT INTO category (id, name, name_hi, name_mr) VALUES (3, 'Canvas Art', 'कैनवास कला', 'कॅनव्हास कला');
INSERT INTO category (id, name, name_hi, name_mr) VALUES (4, 'Woodwork', 'लकड़ी का काम', 'लाकडी काम');
INSERT INTO category (id, name, name_hi, name_mr) VALUES (5, 'Jewelry', 'गहने', 'दागिने');

INSERT INTO artist (id, name, bio, email, profile_picture_url, approved) VALUES (1, 'Anya Sharma', 'Ceramicist inspired by nature.', 'anya@artisan.com', '/images/artists/Anya Sharma.png', true);
INSERT INTO artist (id, name, bio, email, profile_picture_url, approved) VALUES (2, 'Benji K.', 'Weaving traditional patterns with modern twist.', 'benji@artisan.com', '/images/artists/Benji K.png', true);
INSERT INTO artist (id, name, bio, email, profile_picture_url, approved) VALUES (3, 'Arun Verma', 'Ceramic Sculptor & Potter', 'arun@artisan.com', '/images/artists/Arun Verma.png', true);
INSERT INTO artist (id, name, bio, email, profile_picture_url, approved) VALUES (4, 'Sara Martinez', 'Digital Canvas Illustrator', 'sara@artisan.com', '/images/artists/Sara Martinez.png', true);

INSERT INTO product (id, name, name_hi, name_mr, description, description_hi, description_mr, price, image_url, image_hash, artist_id, category_id, inventory_count, is_available) VALUES (1, 'Blue Glaze Mug', 'ब्लू ग्लेज मग', 'ब्लू ग्लेझ मग', 'Hand-thrown stoneware mug.', 'हाथ से बना पत्थर का मग।', 'हाताने बनवलेला स्टोनवेअर मग।', 900.00, '/images/blue glaze ug.jpg', null, 1, 1, 20, true);
INSERT INTO product (id, name, name_hi, name_mr, description, description_hi, description_mr, price, image_url, image_hash, artist_id, category_id, inventory_count, is_available) VALUES (2, 'Woven Wall Hanging', 'बुनी हुई दीवार की सजावट', 'विणलेली वॉल हँगिंग', 'Wool and cotton tapestry.', 'ऊन और सूती टेपेस्ट्री।', 'लोकर आणि सुती टेपेस्ट्री।', 5000.00, '/images/woven wall hanging.jpg', null, 2, 2, 20, true);
INSERT INTO product (id, name, name_hi, name_mr, description, description_hi, description_mr, price, image_url, image_hash, artist_id, category_id, inventory_count, is_available) VALUES (3, 'Mountain Landscape Painting', 'पहाड़ों का सुंदर दृश्य', 'डोंगराळ निसर्ग चित्र', 'Hand-painted oil landscape.', 'हाथ से पेंट किया गया पहाड़ों का दृश्य।', 'हाताने रंगवलेले डोंगराळ निसर्ग चित्र।', 9000.00, '/images/mountain landscape painting.jpg', null, 3, 3, 10, true);
INSERT INTO product (id, name, name_hi, name_mr, description, description_hi, description_mr, price, image_url, image_hash, artist_id, category_id, inventory_count, is_available) VALUES (4, 'Hand-Carved Walnut Bowl', 'हाथ से नक्काशीदार अखरोट का कटोरा', 'हाताने कोरीव काम केलेले अक्रोडाचे भांडे', 'Sustainable walnut wood bowl.', 'टिकाऊ अखरोट की लकड़ी का कटोरा।', 'शाश्वत अक्रोडाच्या लाकडाचे भांडे।', 4500.00, '/images/hand carved walnut bowl.jpg', null, 4, 4, 20, true);
INSERT INTO product (id, name, name_hi, name_mr, description, description_hi, description_mr, price, image_url, image_hash, artist_id, category_id, inventory_count, is_available) VALUES (5, 'Terracotta Vase', 'टेराकोटा फूलदान', 'टेराकोटा फुलदाणी', 'Rustic clay vase for dried flowers.', 'सूखे फूलों के लिए देहाती मिट्टी का फूलदान।', 'सुकलेल्या फुलांसाठी मातीची फुलदाणी।', 2200.00, '/images/teracotta vase.jpg', null, 1, 1, 15, true);
INSERT INTO product (id, name, name_hi, name_mr, description, description_hi, description_mr, price, image_url, image_hash, artist_id, category_id, inventory_count, is_available) VALUES (6, 'Geometric Brass Earrings', 'ज्यामितीय पीतल की झुमकी', 'भौमितिक पितळाचे दागिने', 'Hand-cut recycled brass.', 'हाथ से कटा हुआ पुनर्नवीनीकरण पीतल।', 'हाताने कापलेले पुनर्नवीनीकरण केलेले पितळ।', 600.00, '/images/geometric brass earisgns.jpg', null, 2, 5, 25, true);

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
