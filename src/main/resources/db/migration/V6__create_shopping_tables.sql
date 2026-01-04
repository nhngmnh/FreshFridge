-- V6: Create shopping tables
-- Migration for Shopping domain

-- Shopping Lists table
CREATE TABLE shopping_lists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    group_id BIGINT NOT NULL,
    assigned_to_user_id BIGINT,           -- User được giao đi chợ
    note TEXT,
    shopping_date DATE,                    -- Ngày dự kiến đi chợ
    status VARCHAR(20) DEFAULT 'PENDING',  -- PENDING, IN_PROGRESS, COMPLETED
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_shopping_lists_group FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE,
    CONSTRAINT fk_shopping_lists_assigned_to FOREIGN KEY (assigned_to_user_id) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT fk_shopping_lists_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_shopping_lists_group_id ON shopping_lists(group_id);
CREATE INDEX idx_shopping_lists_assigned_to ON shopping_lists(assigned_to_user_id);
CREATE INDEX idx_shopping_lists_status ON shopping_lists(status);

-- Shopping List Items table
CREATE TABLE shopping_list_items (
    id BIGSERIAL PRIMARY KEY,
    shopping_list_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    unit_id BIGINT NOT NULL,
    quantity DECIMAL(10,2) NOT NULL DEFAULT 1,
    actual_quantity DECIMAL(10,2),         -- Số lượng thực tế đã mua
    is_purchased BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_shopping_items_list FOREIGN KEY (shopping_list_id) REFERENCES shopping_lists(id) ON DELETE CASCADE,
    CONSTRAINT fk_shopping_items_food FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE,
    CONSTRAINT fk_shopping_items_unit FOREIGN KEY (unit_id) REFERENCES units(id),
    CONSTRAINT uk_shopping_items_list_food UNIQUE (shopping_list_id, food_id)
);

CREATE INDEX idx_shopping_list_items_list_id ON shopping_list_items(shopping_list_id);
CREATE INDEX idx_shopping_list_items_food_id ON shopping_list_items(food_id);

-- Comments:
-- shopping_lists: Danh sách mua sắm của group, được giao cho một user cụ thể
-- shopping_list_items: Các mục trong danh sách mua sắm với thông tin về số lượng cần mua và đã mua
-- Status flow: PENDING -> IN_PROGRESS -> COMPLETED
