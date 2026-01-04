-- V10: Insert food images from Cloudinary
-- Migration to add image URLs to existing foods

-- Update images for Rau củ (Vegetables)
UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449891/c%C3%A0_chua_wylja9.jpg' 
WHERE name = 'Cà chua' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449890/c%C3%A0_r%E1%BB%91t_etmaq4.jpg' 
WHERE name = 'Cà rốt' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449889/Khoai_T%C3%A2y_wpgpkq.jpg' 
WHERE name = 'Khoai tây' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449887/H%C3%A0nh_t%C3%A2y_qpttwr.jpg' 
WHERE name = 'Hành tây' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449886/t%E1%BB%8Fi_ic3rlo.jpg' 
WHERE name = 'Tỏi' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449884/B%E1%BA%AFp_c%E1%BA%A3i_vkhcwp.jpg' 
WHERE name = 'Bắp cải' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449883/rau_mu%E1%BB%91ng_v3dvbg.png' 
WHERE name = 'Rau muống' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449881/rau_c%E1%BA%A3i_ghjdn9.png' 
WHERE name = 'Rau cải' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449794/d%C6%B0a_chu%E1%BB%99t_ijk5lc.jpg' 
WHERE name = 'Dưa chuột' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449880/B%C3%AD_%C4%91ao_bmnncz.png' 
WHERE name = 'Bí đao' AND group_id IS NULL;

-- Update images for Thịt (Meat)
UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449877/Th%E1%BB%8Bt_heo_do9nid.jpg' 
WHERE name = 'Thịt heo' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449795/th%E1%BB%8Bt_b%C3%B2_nawmxl.png' 
WHERE name = 'Thịt bò' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449876/Th%E1%BB%8Bt_g%C3%A0_l16qjy.jpg' 
WHERE name = 'Thịt gà' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449875/th%E1%BB%8Bt_v%E1%BB%8Bt_oj2yob.png' 
WHERE name = 'Thịt vịt' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449797/x%C6%B0%C6%A1ng_heo_kp7buj.png' 
WHERE name = 'Xương heo' AND group_id IS NULL;

-- Update images for Hải sản (Seafood)
UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449874/c%C3%A1_thu_tf3ptv.jpg' 
WHERE name = 'Cá thu' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767450351/ca-thu-dao-2_zptaou.jpg' 
WHERE name = 'Cá hồi' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449871/t%C3%B4m_cbubmk.jpg' 
WHERE name = 'Tôm' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449870/m%E1%BB%B1c_tnkzpq.jpg' 
WHERE name = 'Mực' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449868/cua_ojcteq.png' 
WHERE name = 'Cua' AND group_id IS NULL;

-- Update images for Trứng & Sữa (Eggs & Dairy)
UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449867/tr%E1%BB%A9ng_g%C3%A0_n5tmkl.jpg' 
WHERE name = 'Trứng gà' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449867/tr%E1%BB%A9ng_g%C3%A0_n5tmkl.jpg' 
WHERE name = 'Trứng vịt' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449865/s%E1%BB%AFa_t%C6%B0%C6%A1i_msr3vs.png' 
WHERE name = 'Sữa tươi' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449796/ph%C3%B4_mai_we9aj0.png' 
WHERE name = 'Phô mai' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449775/B%C6%A1_q52uck.png' 
WHERE name = 'Bơ' AND group_id IS NULL;

-- Update images for Gia vị (Seasonings)
UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449808/mu%E1%BB%91i_apnodf.jpg' 
WHERE name = 'Muối' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767450883/%C4%91%C6%B0%E1%BB%9Dng_vvgipn.jpg' 
WHERE name = 'Đường' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449794/n%C6%B0%E1%BB%9Bc_m%E1%BA%AFm_imlmou.png' 
WHERE name = 'Nước mắm' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449794/d%E1%BA%A7u_%C4%83n_og1f7a.png' 
WHERE name = 'Dầu ăn' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449807/h%E1%BA%A1t_n%C3%AAm_utjirf.jpg' 
WHERE name = 'Hạt nêm' AND group_id IS NULL;

-- Update images for Đồ khô (Dry goods)
UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449806/g%E1%BA%A1o_yefpbz.jpg' 
WHERE name = 'Gạo' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449806/g%E1%BA%A1o_yefpbz.jpg' 
WHERE name = 'Mì gói' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449804/mi%E1%BA%BFn_blhp39.jpg' 
WHERE name = 'Bún khô' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449804/mi%E1%BA%BFn_blhp39.jpg' 
WHERE name = 'Miến' AND group_id IS NULL;

-- Update images for Đồ uống (Beverages)
UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449804/mi%E1%BA%BFn_blhp39.jpg' 
WHERE name = 'Nước ngọt' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449802/n%C6%B0%E1%BB%9Bc_cam_yfpleg.jpg' 
WHERE name = 'Nước cam' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449801/tr%C3%A0_xanh_r8eyu0.jpg' 
WHERE name = 'Trà xanh' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767451163/c%C3%A0_ph%C3%AA_bamikc.jpg' 
WHERE name = 'Cà phê' AND group_id IS NULL;

-- Update images for Đông lạnh (Frozen)
UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449799/kem_s9e9us.jpg' 
WHERE name = 'Kem' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449796/x%C3%BAc_x%C3%ADch_ivbpwh.png' 
WHERE name = 'Xúc xích' AND group_id IS NULL;

UPDATE foods SET image_url = 'https://res.cloudinary.com/dhqw0mida/image/upload/v1767449798/h%C3%A1_c%E1%BA%A3o_aojvgx.jpg' 
WHERE name = 'Há cảo' AND group_id IS NULL;

-- Update updated_at timestamp for all modified records
UPDATE foods SET updated_at = CURRENT_TIMESTAMP 
WHERE group_id IS NULL AND image_url IS NOT NULL;
