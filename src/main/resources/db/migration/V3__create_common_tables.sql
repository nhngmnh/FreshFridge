-- V3: Create categories, units, notifications, and system_logs tables
-- Migration for Common domain

-- Categories table (danh mục thực phẩm)
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed data for categories
INSERT INTO categories (name, description) VALUES 
('Rau củ', 'Các loại rau củ quả'),
('Thịt', 'Các loại thịt'),
('Hải sản', 'Tôm, cá, mực...'),
('Trứng & Sữa', 'Trứng, sữa, phô mai'),
('Gia vị', 'Các loại gia vị nấu ăn'),
('Đồ khô', 'Gạo, mì, bún khô'),
('Đồ uống', 'Nước ngọt, nước ép'),
('Đông lạnh', 'Thực phẩm đông lạnh'),
('Khác', 'Các loại khác');

-- Units table (đơn vị đo lường)
CREATE TABLE units (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    abbreviation VARCHAR(10),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed data for units
INSERT INTO units (name, abbreviation) VALUES 
('Kilogram', 'kg'),
('Gram', 'g'),
('Lít', 'l'),
('Mililít', 'ml'),
('Cái', 'cái'),
('Quả', 'quả'),
('Gói', 'gói'),
('Hộp', 'hộp'),
('Chai', 'chai'),
('Bó', 'bó'),
('Khay', 'khay'),
('Túi', 'túi'),
('Lon', 'lon');

-- Notifications table (thông báo)
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL,
    reference_type VARCHAR(50),
    reference_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes for notifications
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(user_id, is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);

-- System logs table (nhật ký hệ thống)
CREATE TABLE system_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    details JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_system_logs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Indexes for system_logs
CREATE INDEX idx_system_logs_user_id ON system_logs(user_id);
CREATE INDEX idx_system_logs_action ON system_logs(action);
CREATE INDEX idx_system_logs_created_at ON system_logs(created_at);

-- Comments
COMMENT ON TABLE categories IS 'Danh mục phân loại thực phẩm';
COMMENT ON TABLE units IS 'Đơn vị đo lường thực phẩm';
COMMENT ON TABLE notifications IS 'Thông báo cho người dùng';
COMMENT ON COLUMN notifications.type IS 'Loại thông báo: EXPIRY_WARNING, EXPIRY_ALERT, SHOPPING_REMINDER, GROUP_INVITE, etc.';
COMMENT ON COLUMN notifications.reference_type IS 'Loại entity tham chiếu: KITCHEN_ITEM, SHOPPING_LIST, GROUP, etc.';
COMMENT ON TABLE system_logs IS 'Nhật ký hoạt động hệ thống';
COMMENT ON COLUMN system_logs.action IS 'Loại hành động: USER_LOGIN, GROUP_CREATE, KITCHEN_ITEM_ADDED, etc.';
