-- V8__Create_meal_plan_table.sql
-- Create meal_plans table

CREATE TABLE meal_plans (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT NOT NULL REFERENCES groups(id) ON DELETE CASCADE,
    recipe_id BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    meal_date DATE NOT NULL,
    meal_name VARCHAR(20) NOT NULL,
    servings INTEGER NOT NULL DEFAULT 1,
    note TEXT,
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better query performance
CREATE INDEX idx_meal_plan_group_date ON meal_plans(group_id, meal_date);
CREATE INDEX idx_meal_plan_recipe ON meal_plans(recipe_id);

-- Add constraint for meal_name values
ALTER TABLE meal_plans ADD CONSTRAINT chk_meal_name 
    CHECK (meal_name IN ('SANG', 'TRUA', 'TOI'));

COMMENT ON TABLE meal_plans IS 'Kế hoạch bữa ăn của nhóm';
COMMENT ON COLUMN meal_plans.group_id IS 'ID của nhóm';
COMMENT ON COLUMN meal_plans.recipe_id IS 'ID của công thức';
COMMENT ON COLUMN meal_plans.meal_date IS 'Ngày của bữa ăn';
COMMENT ON COLUMN meal_plans.meal_name IS 'Loại bữa ăn: SANG (sáng), TRUA (trưa), TOI (tối)';
COMMENT ON COLUMN meal_plans.servings IS 'Số phần ăn';
COMMENT ON COLUMN meal_plans.note IS 'Ghi chú thêm';
COMMENT ON COLUMN meal_plans.created_by IS 'ID người tạo';
