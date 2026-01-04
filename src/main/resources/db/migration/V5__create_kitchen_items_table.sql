-- V5: Create kitchen_items table
-- Migration for Kitchen domain

CREATE TABLE kitchen_items (
    id BIGSERIAL PRIMARY KEY,
    food_id BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    unit_id BIGINT NOT NULL,
    quantity DECIMAL(10,2) NOT NULL DEFAULT 1,
    use_within INTEGER,                      -- Days until expiry (optional)
    expiry_date DATE,                        -- Calculated: created_at + use_within days
    note TEXT,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_kitchen_items_food FOREIGN KEY (food_id) REFERENCES foods(id) ON DELETE CASCADE,
    CONSTRAINT fk_kitchen_items_group FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE,
    CONSTRAINT fk_kitchen_items_unit FOREIGN KEY (unit_id) REFERENCES units(id),
    CONSTRAINT fk_kitchen_items_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    CONSTRAINT uk_kitchen_items_food_group UNIQUE (food_id, group_id)
);

-- Indexes for performance
CREATE INDEX idx_kitchen_items_group_id ON kitchen_items(group_id);
CREATE INDEX idx_kitchen_items_food_id ON kitchen_items(food_id);
CREATE INDEX idx_kitchen_items_expiry_date ON kitchen_items(expiry_date);
CREATE INDEX idx_kitchen_items_created_by ON kitchen_items(created_by);

-- Comment: kitchen_items tracks food items in a group's kitchen/refrigerator
-- The expiry_date is calculated as created_at + use_within days when use_within is provided
-- If use_within is NULL, expiry_date can be set directly or left NULL (non-perishable)
