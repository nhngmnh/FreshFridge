-- V11: Insert recipes and recipe ingredients
-- Migration to add 50 Vietnamese recipes with ingredients

-- Insert 50 recipes with explicit IDs
INSERT INTO recipes (id, name, description, html_content, is_system, created_at) VALUES
-- Món canh (Soups)
(1, 'Canh chua cá', 'Canh chua cá thơm ngon đậm đà', '<p>Làm sạch cá, ướpgia vị. Nấu nước dùng với cà chua, dứa. Cho cá vào nấu chín.</p>', TRUE, CURRENT_TIMESTAMP),
(2, 'Canh bí đao', 'Canh bí đao thanh mát bổ dưỡng', '<p>Gọt vỏ bí đao, cắt miếng. Nấu nước sôi, cho bí vào. Nêm nếm gia vị.</p>', TRUE, CURRENT_TIMESTAMP),
(3, 'Canh cải thịt băm', 'Canh rau cải thịt băm nhanh gọn', '<p>Xào thịt băm thơm. Cho nước vào nấu sôi. Thêm rau cải và gia vị.</p>', TRUE, CURRENT_TIMESTAMP),
(4, 'Canh chua tôm', 'Canh chua tôm chua cay ngon miệng', '<p>Làm sạch tôm. Nấu nước dùng với cà chua. Cho tôm và rau vào nấu.</p>', TRUE, CURRENT_TIMESTAMP),
(5, 'Canh mực chua', 'Canh mực chua thanh ngon', '<p>Làm sạch mực, cắt khoanh. Nấu nước dùng chua. Cho mực vào nấu nhanh.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món xào (Stir-fried)
(6, 'Thịt heo xào cà chua', 'Thịt heo xào cà chua đưa cơm', '<p>Ướp thịt với gia vị. Xào thơm tỏi, cho thịt vào xào. Thêm cà chua xào chín.</p>', TRUE, CURRENT_TIMESTAMP),
(7, 'Thịt bò xào rau muống', 'Thịt bò xào rau muống giòn ngọt', '<p>Ướp thịt bò mềm. Xào thơm tỏi, cho thịt bò xào nhanh. Thêm rau muống xào vừa.</p>', TRUE, CURRENT_TIMESTAMP),
(8, 'Gà xào gừng', 'Gà xào gừng thơm nồng', '<p>Thái thịt gà miếng vừa. Xào gừng thơm. Cho gà vào xào chín với gia vị.</p>', TRUE, CURRENT_TIMESTAMP),
(9, 'Mực xào chua ngọt', 'Mực xào chua ngọt hấp dẫn', '<p>Làm sạch mực, cắt miếng. Xào thơm hành. Cho mực và sốt chua ngọt xào nhanh.</p>', TRUE, CURRENT_TIMESTAMP),
(10, 'Tôm xào bông cải', 'Tôm xào bông cải dinh dưỡng', '<p>Làm sạch tôm. Luộc bông cải. Xào tôm với bông cải và gia vị.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món chiên/rán (Fried)
(11, 'Trứng chiên', 'Trứng chiên đơn giản nhanh gọn', '<p>Đập trứng vào bát đánh đều. Làm nóng dầu. Chiên trứng vàng 2 mặt.</p>', TRUE, CURRENT_TIMESTAMP),
(12, 'Cá chiên nước mắm', 'Cá chiên nước mắm thơm ngon', '<p>Rửa sạch cá, ướp gia vị. Chiên cá vàng giòn. Chan nước mắm pha.</p>', TRUE, CURRENT_TIMESTAMP),
(13, 'Thịt heo chiên xù', 'Thịt heo chiên xù giòn rụm', '<p>Thái thịt mỏng, ướp gia vị. Tẩm bột chiên xù. Chiên vàng giòn.</p>', TRUE, CURRENT_TIMESTAMP),
(14, 'Gà rán', 'Gà rán giòn tan thơm lừng', '<p>Ướp gà với gia vị. Tẩm bột chiên giòn. Chiên ngập dầu đến vàng.</p>', TRUE, CURRENT_TIMESTAMP),
(15, 'Khoai tây chiên', 'Khoai tây chiên giòn rụm', '<p>Gọt khoai tây, cắt sợi. Ngâm nước lạnh. Chiên 2 lần để giòn.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món luộc/hấp (Boiled/Steamed)
(16, 'Thịt heo luộc', 'Thịt heo luộc mềm ngọt', '<p>Rửa sạch thịt. Luộc với hành tỏi. Vớt ra để nguội, thái lát.</p>', TRUE, CURRENT_TIMESTAMP),
(17, 'Gà luộc', 'Gà luộc nguyên con thơm ngon', '<p>Rửa sạch gà. Luộc trong nước sôi với gừng. Luộc chín vớt ra.</p>', TRUE, CURRENT_TIMESTAMP),
(18, 'Trứng luộc', 'Trứng luộc dinh dưỡng', '<p>Cho trứng vào nồi nước lạnh. Đun sôi 8-10 phút. Ngâm nước lạnh.</p>', TRUE, CURRENT_TIMESTAMP),
(19, 'Tôm luộc', 'Tôm luộc tươi ngọt', '<p>Rửa sạch tôm. Luộc nhanh trong nước sôi. Vớt ra ngâm nước đá.</p>', TRUE, CURRENT_TIMESTAMP),
(20, 'Bắp cải luộc', 'Bắp cải luộc giòn ngọt', '<p>Rửa sạch bắp cải, cắt múi. Luộc trong nước sôi. Vớt ra để ráo.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món kho (Braised)
(21, 'Thịt kho tàu', 'Thịt kho tàu đậm đà', '<p>Luộc sơ thịt. Kho với nước dừa, nước mắm. Kho đến thịt mềm thấm gia vị.</p>', TRUE, CURRENT_TIMESTAMP),
(22, 'Cá kho tộ', 'Cá kho tộ thơm ngon đưa cơm', '<p>Rửa sạch cá, ướp gia vị. Kho với nước mắm đường. Kho đến cá chín săn.</p>', TRUE, CURRENT_TIMESTAMP),
(23, 'Thịt kho trứng', 'Thịt kho trứng đậm vị', '<p>Luộc chín trứng. Kho thịt với nước dừa. Cho trứng vào kho cùng.</p>', TRUE, CURRENT_TIMESTAMP),
(24, 'Gà kho gừng', 'Gà kho gừng ấm bụng', '<p>Chặt gà miếng vừa. Kho với gừng, nước mắm. Kho đến gà mềm thấm vị.</p>', TRUE, CURRENT_TIMESTAMP),
(25, 'Cá kho riềng', 'Cá kho riềng thơm nồng', '<p>Làm sạch cá, ướp gia vị. Kho với riềng băm. Kho đến cá chín săn.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món nướng (Grilled)
(26, 'Thịt heo nướng', 'Thịt heo nướng thơm lừng', '<p>Ướp thịt với gia vị qua đêm. Nướng trên than hồng. Nướng chín 2 mặt.</p>', TRUE, CURRENT_TIMESTAMP),
(27, 'Gà nướng muối ớt', 'Gà nướng muối ớt cay thơm', '<p>Ướp gà với muối ớt. Nướng lò hoặc nướng than. Nướng đều đến vàng.</p>', TRUE, CURRENT_TIMESTAMP),
(28, 'Cá nướng', 'Cá nướng thơm phức', '<p>Rửa sạch cá, ướp gia vị. Nướng trên than hồng. Nướng chín giòn.</p>', TRUE, CURRENT_TIMESTAMP),
(29, 'Tôm nướng', 'Tôm nướng tươi ngon', '<p>Làm sạch tôm, ướp gia vị. Nướng trên than hồng. Nướng đỏ đều.</p>', TRUE, CURRENT_TIMESTAMP),
(30, 'Cua nướng', 'Cua nướng thơm béo', '<p>Làm sạch cua. Nướng trên bếp than. Nướng chín đỏ.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món cơm (Rice dishes)
(31, 'Cơm chiên dương châu', 'Cơm chiên dương châu nhiều topping', '<p>Chuẩn bị cơm nguội. Xào trứng, thịt, tôm. Cho cơm vào xào đều.</p>', TRUE, CURRENT_TIMESTAMP),
(32, 'Cơm chiên thập cẩm', 'Cơm chiên thập cẩm đầy đủ', '<p>Chuẩn bị cơm nguội và topping. Xào thơm topping. Trộn cơm xào đều.</p>', TRUE, CURRENT_TIMESTAMP),
(33, 'Cơm gà xối mỡ', 'Cơm gà xối mỡ hấp dẫn', '<p>Nấu cơm với nước luộc gà. Xé gà luộc. Xối mỡ hành lên cơm.</p>', TRUE, CURRENT_TIMESTAMP),
(34, 'Cơm gà Hải Nam', 'Cơm gà Hải Nam thơm béo', '<p>Nấu cơm với nước luộc gà. Luộc gà thơm mềm. Chấm mắm gừng.</p>', TRUE, CURRENT_TIMESTAMP),
(35, 'Cơm trắng', 'Cơm trắng đơn giản', '<p>Vo gạo sạch. Cho vào nồi cơm điện. Nấu chín và xới cơm.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món bún/phở (Noodle soups)
(36, 'Phở bò', 'Phở bò truyền thống Hà Nội', '<p>Ninh xương bò lâu giờ. Luộc bánh phở. Chan nước dùng, cho thịt bò.</p>', TRUE, CURRENT_TIMESTAMP),
(37, 'Phở gà', 'Phở gà thanh đạm ngọt vị', '<p>Hầm xương gà lâu. Luộc bánh phở. Chan nước dùng, cho thịt gà.</p>', TRUE, CURRENT_TIMESTAMP),
(38, 'Bún bò', 'Bún bò Huế cay nồng', '<p>Ninh xương heo, bò. Luộc bún tươi. Chan nước dùng cay thơm.</p>', TRUE, CURRENT_TIMESTAMP),
(39, 'Bún riêu cua', 'Bún riêu cua cà chua đậm đà', '<p>Nấu nước dùng với cà chua. Làm riêu cua. Chan lên bún tươi.</p>', TRUE, CURRENT_TIMESTAMP),
(40, 'Bún chả', 'Bún chả Hà Nội nổi tiếng', '<p>Nướng chả heo thơm. Pha nước mắm chua ngọt. Ăn kèm bún tươi.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món mì/miến (Noodles)
(41, 'Mì xào giòn', 'Mì xào giòn giòn tan', '<p>Chiên mì vàng giòn. Xào topping. Xếp mì, chan topping lên.</p>', TRUE, CURRENT_TIMESTAMP),
(42, 'Mì xào mềm', 'Mì xào mềm hấp dẫn', '<p>Luộc mì mềm. Xào thơm topping. Cho mì vào xào đều.</p>', TRUE, CURRENT_TIMESTAMP),
(43, 'Miến xào cua', 'Miến xào cua thơm béo', '<p>Ngâm miến mềm. Xào thơm thịt cua. Cho miến vào xào đều.</p>', TRUE, CURRENT_TIMESTAMP),
(44, 'Miến gà', 'Miến gà nấm thơm ngon', '<p>Hầm nước dùng gà. Ngâm miến mềm. Chan nước dùng lên miến.</p>', TRUE, CURRENT_TIMESTAMP),
(45, 'Mì trộn', 'Mì trộn nhanh gọn', '<p>Luộc mì chín. Vớt ra để ráo. Trộn với nước tương và topping.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món salad/gỏi (Salads)
(46, 'Gỏi gà', 'Gỏi gà trộn rau thơm', '<p>Xé gà luộc. Trộn với rau thơm, hành tây. Chan nước mắm chua ngọt.</p>', TRUE, CURRENT_TIMESTAMP),
(47, 'Gỏi cuốn tôm thịt', 'Gỏi cuốn tôm thịt thanh mát', '<p>Luộc tôm thịt. Cuốn bánh tráng với rau. Chấm tương đậu phộng.</p>', TRUE, CURRENT_TIMESTAMP),
(48, 'Salad rau trộn', 'Salad rau trộn tươi mát', '<p>Rửa sạch rau củ. Cắt nhỏ vừa ăn. Trộn đều với sốt salad.</p>', TRUE, CURRENT_TIMESTAMP),
(49, 'Gỏi ngó sen tôm', 'Gỏi ngó sen tôm giòn ngọt', '<p>Luộc tôm và ngó sen. Trộn với rau thơm. Chan nước mắm chua ngọt.</p>', TRUE, CURRENT_TIMESTAMP),

-- Món đồ uống (Beverages)
(50, 'Trà sữa', 'Trà sữa thơm ngon', '<p>Pha trà đen đậm. Thêm sữa tươi và đường. Thêm đá uống mát.</p>', TRUE, CURRENT_TIMESTAMP),
(51, 'Cà phê sữa đá', 'Cà phê sữa đá truyền thống', '<p>Pha cà phê đậm. Thêm sữa đặc. Cho đá uống mát.</p>', TRUE, CURRENT_TIMESTAMP);

-- Reset sequence to continue from the last inserted ID
SELECT setval('recipes_id_seq', 51, true);

-- Now insert recipe ingredients for each recipe
-- Recipe 1: Canh chua cá
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(1, 16, 0.5, 1, TRUE, 'Cá thu tươi'),
(1, 1, 2, 6, FALSE, 'Cà chua chín'),
(1, 5, 2, 5, FALSE, 'Tỏi băm'),
(1, 28, 2, 4, FALSE, 'Nước mắm'),
(1, 27, 1, 4, FALSE, 'Đường'),
(1, 30, 1, 4, FALSE, 'Hạt nêm');

-- Recipe 2: Canh bí đao
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(2, 10, 0.3, 1, TRUE, 'Bí đao tươi'),
(2, 11, 0.2, 1, FALSE, 'Thịt heo băm'),
(2, 5, 2, 5, FALSE, 'Tỏi băm'),
(2, 28, 1, 4, FALSE, 'Nước mắm'),
(2, 30, 1, 4, FALSE, 'Hạt nêm');

-- Recipe 3: Canh cải thịt băm
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(3, 8, 0.3, 1, TRUE, 'Rau cải xanh'),
(3, 11, 0.2, 1, TRUE, 'Thịt heo băm'),
(3, 5, 2, 5, FALSE, 'Tỏi băm'),
(3, 28, 1, 4, FALSE, 'Nước mắm'),
(3, 30, 1, 4, FALSE, 'Hạt nêm');

-- Recipe 4: Canh chua tôm
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(4, 18, 0.3, 1, TRUE, 'Tôm tươi'),
(4, 1, 2, 6, FALSE, 'Cà chua'),
(4, 5, 2, 5, FALSE, 'Tỏi băm'),
(4, 28, 2, 4, FALSE, 'Nước mắm'),
(4, 27, 1, 4, FALSE, 'Đường');

-- Recipe 5: Canh mực chua
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(5, 19, 0.4, 1, TRUE, 'Mực tươi'),
(5, 1, 2, 6, FALSE, 'Cà chua'),
(5, 5, 2, 5, FALSE, 'Tỏi băm'),
(5, 28, 2, 4, FALSE, 'Nước mắm'),
(5, 27, 1, 4, FALSE, 'Đường');

-- Recipe 6: Thịt heo xào cà chua
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(6, 11, 0.3, 1, TRUE, 'Thịt heo thái lát'),
(6, 1, 3, 6, TRUE, 'Cà chua chín'),
(6, 5, 3, 5, FALSE, 'Tỏi băm'),
(6, 4, 0.5, 6, FALSE, 'Hành tây'),
(6, 29, 2, 4, FALSE, 'Dầu ăn'),
(6, 28, 1, 4, FALSE, 'Nước mắm');

-- Recipe 7: Thịt bò xào rau muống
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(7, 12, 0.3, 1, TRUE, 'Thịt bò thái lát'),
(7, 7, 0.3, 1, TRUE, 'Rau muống'),
(7, 5, 3, 5, FALSE, 'Tỏi băm'),
(7, 29, 2, 4, FALSE, 'Dầu ăn'),
(7, 28, 1, 4, FALSE, 'Nước mắm');

-- Recipe 8: Gà xào gừng
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(8, 13, 0.5, 1, TRUE, 'Thịt gà'),
(8, 5, 2, 5, FALSE, 'Tỏi băm'),
(8, 4, 0.5, 6, FALSE, 'Hành tây'),
(8, 29, 2, 4, FALSE, 'Dầu ăn'),
(8, 28, 2, 4, FALSE, 'Nước mắm');

-- Recipe 9: Mực xào chua ngọt
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(9, 19, 0.4, 1, TRUE, 'Mực tươi'),
(9, 1, 2, 6, FALSE, 'Cà chua'),
(9, 4, 0.5, 6, FALSE, 'Hành tây'),
(9, 27, 2, 4, FALSE, 'Đường'),
(9, 28, 2, 4, FALSE, 'Nước mắm'),
(9, 29, 2, 4, FALSE, 'Dầu ăn');

-- Recipe 10: Tôm xào bông cải
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(10, 18, 0.3, 1, TRUE, 'Tôm tươi'),
(10, 6, 0.3, 1, FALSE, 'Bắp cải'),
(10, 5, 2, 5, FALSE, 'Tỏi băm'),
(10, 29, 2, 4, FALSE, 'Dầu ăn'),
(10, 28, 1, 4, FALSE, 'Nước mắm');

-- Recipe 11: Trứng chiên
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(11, 21, 3, 5, TRUE, 'Trứng gà'),
(11, 29, 2, 4, FALSE, 'Dầu ăn'),
(11, 26, 0.5, 2, FALSE, 'Muối');

-- Recipe 12: Cá chiên nước mắm
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(12, 16, 0.5, 1, TRUE, 'Cá thu'),
(12, 29, 3, 4, FALSE, 'Dầu chiên'),
(12, 28, 2, 4, FALSE, 'Nước mắm'),
(12, 27, 1, 4, FALSE, 'Đường'),
(12, 5, 2, 5, FALSE, 'Tỏi băm');

-- Recipe 13: Thịt heo chiên xù
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(13, 11, 0.4, 1, TRUE, 'Thịt heo'),
(13, 21, 2, 5, FALSE, 'Trứng gà'),
(13, 29, 4, 4, FALSE, 'Dầu chiên'),
(13, 26, 1, 2, FALSE, 'Muối');

-- Recipe 14: Gà rán
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(14, 13, 0.5, 1, TRUE, 'Thịt gà'),
(14, 21, 1, 5, FALSE, 'Trứng gà'),
(14, 29, 5, 4, FALSE, 'Dầu chiên'),
(14, 26, 1, 2, FALSE, 'Muối');

-- Recipe 15: Khoai tây chiên
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(15, 3, 0.5, 1, TRUE, 'Khoai tây'),
(15, 29, 4, 4, FALSE, 'Dầu chiên'),
(15, 26, 1, 2, FALSE, 'Muối');

-- Recipe 16: Thịt heo luộc
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(16, 11, 0.5, 1, TRUE, 'Thịt heo'),
(16, 4, 0.5, 6, FALSE, 'Hành tây'),
(16, 5, 3, 5, FALSE, 'Tỏi'),
(16, 26, 1, 2, FALSE, 'Muối');

-- Recipe 17: Gà luộc
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(17, 13, 1, 1, TRUE, 'Gà nguyên con'),
(17, 4, 0.5, 6, FALSE, 'Hành tây'),
(17, 5, 3, 5, FALSE, 'Tỏi'),
(17, 26, 1, 2, FALSE, 'Muối');

-- Recipe 18: Trứng luộc
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(18, 21, 6, 5, TRUE, 'Trứng gà'),
(18, 26, 0.5, 2, FALSE, 'Muối');

-- Recipe 19: Tôm luộc
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(19, 18, 0.5, 1, TRUE, 'Tôm tươi'),
(19, 5, 2, 5, FALSE, 'Tỏi'),
(19, 26, 1, 2, FALSE, 'Muối');

-- Recipe 20: Bắp cải luộc
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(20, 6, 0.5, 1, TRUE, 'Bắp cải'),
(20, 26, 1, 2, FALSE, 'Muối');

-- Recipe 21: Thịt kho tàu
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(21, 11, 0.5, 1, TRUE, 'Thịt heo ba chỉ'),
(21, 21, 4, 5, FALSE, 'Trứng gà'),
(21, 28, 3, 4, FALSE, 'Nước mắm'),
(21, 27, 2, 4, FALSE, 'Đường'),
(21, 5, 3, 5, FALSE, 'Tỏi băm');

-- Recipe 22: Cá kho tộ
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(22, 16, 0.5, 1, TRUE, 'Cá thu'),
(22, 28, 3, 4, FALSE, 'Nước mắm'),
(22, 27, 2, 4, FALSE, 'Đường'),
(22, 5, 3, 5, FALSE, 'Tỏi băm'),
(22, 4, 0.5, 6, FALSE, 'Hành tây');

-- Recipe 23: Thịt kho trứng
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(23, 11, 0.5, 1, TRUE, 'Thịt heo'),
(23, 21, 6, 5, TRUE, 'Trứng gà'),
(23, 28, 3, 4, FALSE, 'Nước mắm'),
(23, 27, 2, 4, FALSE, 'Đường'),
(23, 5, 3, 5, FALSE, 'Tỏi băm');

-- Recipe 24: Gà kho gừng
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(24, 13, 0.5, 1, TRUE, 'Thịt gà'),
(24, 28, 3, 4, FALSE, 'Nước mắm'),
(24, 27, 2, 4, FALSE, 'Đường'),
(24, 5, 3, 5, FALSE, 'Tỏi băm');

-- Recipe 25: Cá kho riềng
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(25, 16, 0.5, 1, TRUE, 'Cá thu'),
(25, 28, 3, 4, FALSE, 'Nước mắm'),
(25, 27, 2, 4, FALSE, 'Đường'),
(25, 5, 3, 5, FALSE, 'Tỏi băm');

-- Recipe 26: Thịt heo nướng
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(26, 11, 0.5, 1, TRUE, 'Thịt heo'),
(26, 28, 2, 4, FALSE, 'Nước mắm'),
(26, 27, 1, 4, FALSE, 'Đường'),
(26, 5, 3, 5, FALSE, 'Tỏi băm'),
(26, 29, 1, 4, FALSE, 'Dầu ăn');

-- Recipe 27: Gà nướng muối ớt
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(27, 13, 0.5, 1, TRUE, 'Thịt gà'),
(27, 26, 2, 2, FALSE, 'Muối'),
(27, 5, 3, 5, FALSE, 'Tỏi băm'),
(27, 29, 2, 4, FALSE, 'Dầu ăn');

-- Recipe 28: Cá nướng
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(28, 16, 0.5, 1, TRUE, 'Cá thu'),
(28, 28, 2, 4, FALSE, 'Nước mắm'),
(28, 5, 3, 5, FALSE, 'Tỏi băm'),
(28, 29, 1, 4, FALSE, 'Dầu ăn');

-- Recipe 29: Tôm nướng
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(29, 18, 0.5, 1, TRUE, 'Tôm tươi'),
(29, 28, 2, 4, FALSE, 'Nước mắm'),
(29, 5, 3, 5, FALSE, 'Tỏi băm'),
(29, 29, 1, 4, FALSE, 'Dầu ăn');

-- Recipe 30: Cua nướng
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(30, 20, 0.5, 1, TRUE, 'Cua tươi'),
(30, 28, 2, 4, FALSE, 'Nước mắm'),
(30, 5, 3, 5, FALSE, 'Tỏi băm'),
(30, 29, 2, 4, FALSE, 'Dầu ăn');

-- Recipe 31: Cơm chiên dương châu
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(31, 31, 0.5, 1, TRUE, 'Gạo nấu cơm'),
(31, 21, 2, 5, FALSE, 'Trứng gà'),
(31, 11, 0.1, 1, FALSE, 'Thịt heo'),
(31, 18, 0.1, 1, FALSE, 'Tôm'),
(31, 5, 2, 5, FALSE, 'Tỏi băm'),
(31, 29, 3, 4, FALSE, 'Dầu ăn'),
(31, 28, 1, 4, FALSE, 'Nước mắm');

-- Recipe 32: Cơm chiên thập cẩm
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(32, 31, 0.5, 1, TRUE, 'Gạo nấu cơm'),
(32, 21, 2, 5, FALSE, 'Trứng gà'),
(32, 13, 0.1, 1, FALSE, 'Thịt gà'),
(32, 41, 0.1, 1, FALSE, 'Xúc xích'),
(32, 5, 2, 5, FALSE, 'Tỏi băm'),
(32, 29, 3, 4, FALSE, 'Dầu ăn');

-- Recipe 33: Cơm gà xối mỡ
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(33, 31, 0.3, 1, TRUE, 'Gạo'),
(33, 13, 0.5, 1, TRUE, 'Gà nguyên con'),
(33, 5, 3, 5, FALSE, 'Tỏi'),
(33, 4, 0.5, 6, FALSE, 'Hành tây'),
(33, 29, 3, 4, FALSE, 'Dầu ăn');

-- Recipe 34: Cơm gà Hải Nam
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(34, 31, 0.3, 1, TRUE, 'Gạo'),
(34, 13, 0.5, 1, TRUE, 'Gà nguyên con'),
(34, 5, 3, 5, FALSE, 'Tỏi'),
(34, 29, 2, 4, FALSE, 'Dầu ăn');

-- Recipe 35: Cơm trắng
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(35, 31, 0.5, 1, TRUE, 'Gạo tẻ');

-- Recipe 36: Phở bò
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(36, 12, 0.3, 1, TRUE, 'Thịt bò'),
(36, 33, 0.3, 1, TRUE, 'Bún khô làm phở'),
(36, 4, 0.5, 6, FALSE, 'Hành tây'),
(36, 5, 2, 5, FALSE, 'Tỏi'),
(36, 28, 2, 4, FALSE, 'Nước mắm'),
(36, 27, 1, 4, FALSE, 'Đường');

-- Recipe 37: Phở gà
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(37, 13, 0.5, 1, TRUE, 'Thịt gà'),
(37, 33, 0.3, 1, TRUE, 'Bún khô làm phở'),
(37, 4, 0.5, 6, FALSE, 'Hành tây'),
(37, 5, 2, 5, FALSE, 'Tỏi'),
(37, 28, 2, 4, FALSE, 'Nước mắm');

-- Recipe 38: Bún bò
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(38, 12, 0.3, 1, TRUE, 'Thịt bò'),
(38, 11, 0.2, 1, FALSE, 'Thịt heo'),
(38, 33, 0.3, 1, TRUE, 'Bún tươi'),
(38, 28, 3, 4, FALSE, 'Nước mắm'),
(38, 27, 1, 4, FALSE, 'Đường');

-- Recipe 39: Bún riêu cua
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(39, 20, 0.3, 1, TRUE, 'Cua đồng'),
(39, 33, 0.3, 1, TRUE, 'Bún tươi'),
(39, 1, 3, 6, FALSE, 'Cà chua'),
(39, 21, 2, 5, FALSE, 'Trứng gà'),
(39, 28, 2, 4, FALSE, 'Nước mắm');

-- Recipe 40: Bún chả
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(40, 11, 0.4, 1, TRUE, 'Thịt heo'),
(40, 33, 0.3, 1, TRUE, 'Bún tươi'),
(40, 28, 3, 4, FALSE, 'Nước mắm'),
(40, 27, 2, 4, FALSE, 'Đường'),
(40, 5, 3, 5, FALSE, 'Tỏi băm');

-- Recipe 41: Mì xào giòn
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(41, 32, 2, 7, TRUE, 'Mì gói'),
(41, 11, 0.1, 1, FALSE, 'Thịt heo'),
(41, 18, 0.1, 1, FALSE, 'Tôm'),
(41, 2, 0.1, 1, FALSE, 'Cà rốt'),
(41, 29, 3, 4, FALSE, 'Dầu ăn'),
(41, 28, 1, 4, FALSE, 'Nước mắm');

-- Recipe 42: Mì xào mềm
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(42, 32, 2, 7, TRUE, 'Mì gói'),
(42, 13, 0.2, 1, FALSE, 'Thịt gà'),
(42, 6, 0.1, 1, FALSE, 'Bắp cải'),
(42, 29, 2, 4, FALSE, 'Dầu ăn'),
(42, 28, 1, 4, FALSE, 'Nước mắm');

-- Recipe 43: Miến xào cua
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(43, 34, 0.2, 1, TRUE, 'Miến'),
(43, 20, 0.2, 1, TRUE, 'Cua'),
(43, 21, 2, 5, FALSE, 'Trứng gà'),
(43, 5, 2, 5, FALSE, 'Tỏi băm'),
(43, 29, 2, 4, FALSE, 'Dầu ăn');

-- Recipe 44: Miến gà
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(44, 34, 0.2, 1, TRUE, 'Miến'),
(44, 13, 0.3, 1, TRUE, 'Thịt gà'),
(44, 5, 2, 5, FALSE, 'Tỏi'),
(44, 28, 2, 4, FALSE, 'Nước mắm');

-- Recipe 45: Mì trộn
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(45, 32, 2, 7, TRUE, 'Mì gói'),
(45, 4, 0.3, 6, FALSE, 'Hành tây'),
(45, 28, 2, 4, FALSE, 'Nước tương'),
(45, 29, 1, 4, FALSE, 'Dầu ăn');

-- Recipe 46: Gỏi gà
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(46, 13, 0.3, 1, TRUE, 'Thịt gà luộc'),
(46, 6, 0.1, 1, FALSE, 'Bắp cải'),
(46, 2, 0.1, 1, FALSE, 'Cà rốt'),
(46, 4, 0.5, 6, FALSE, 'Hành tây'),
(46, 28, 2, 4, FALSE, 'Nước mắm'),
(46, 27, 1, 4, FALSE, 'Đường');

-- Recipe 47: Gỏi cuốn tôm thịt
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(47, 18, 0.2, 1, TRUE, 'Tôm luộc'),
(47, 11, 0.2, 1, TRUE, 'Thịt heo luộc'),
(47, 33, 0.1, 1, FALSE, 'Bún tươi'),
(47, 9, 0.1, 1, FALSE, 'Dưa chuột');

-- Recipe 48: Salad rau trộn
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(48, 6, 0.1, 1, TRUE, 'Bắp cải'),
(48, 2, 0.1, 1, FALSE, 'Cà rốt'),
(48, 1, 2, 6, FALSE, 'Cà chua'),
(48, 9, 1, 6, FALSE, 'Dưa chuột'),
(48, 27, 1, 4, FALSE, 'Đường'),
(48, 29, 1, 4, FALSE, 'Dầu olive');

-- Recipe 49: Gỏi ngó sen tôm
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(49, 18, 0.2, 1, TRUE, 'Tôm luộc'),
(49, 2, 0.1, 1, FALSE, 'Cà rốt'),
(49, 28, 2, 4, FALSE, 'Nước mắm'),
(49, 27, 1, 4, FALSE, 'Đường');

-- Recipe 50: Trà sữa
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(50, 37, 1, 7, TRUE, 'Trà xanh'),
(50, 23, 0.2, 3, TRUE, 'Sữa tươi'),
(50, 27, 2, 4, FALSE, 'Đường');

-- Recipe 51: Cà phê sữa đá
INSERT INTO recipe_ingredients (recipe_id, food_id, quantity, unit_id, is_main_ingredient, note) VALUES
(51, 38, 3, 4, TRUE, 'Cà phê'),
(51, 23, 0.1, 3, TRUE, 'Sữa đặc'),
(51, 27, 1, 4, FALSE, 'Đường');

-- Comments
COMMENT ON TABLE recipes IS 'Danh sách công thức nấu ăn';
COMMENT ON COLUMN recipes.is_system IS 'TRUE = công thức hệ thống, FALSE = công thức do người dùng tạo';
COMMENT ON TABLE recipe_ingredients IS 'Danh sách nguyên liệu cho mỗi công thức';
COMMENT ON COLUMN recipe_ingredients.is_main_ingredient IS 'TRUE = nguyên liệu chính, FALSE = nguyên liệu phụ';
