-- V7__Create_recipe_tables.sql

-- Recipes table
CREATE TABLE recipes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    html_content TEXT,
    image_url TEXT,
    group_id BIGINT REFERENCES groups(id) ON DELETE CASCADE,
    is_system BOOLEAN DEFAULT FALSE,
    created_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_recipes_group_id ON recipes(group_id);
CREATE INDEX idx_recipes_name ON recipes(name);
CREATE INDEX idx_recipes_is_system ON recipes(is_system);

-- Recipe Ingredients table
CREATE TABLE recipe_ingredients (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    food_id BIGINT NOT NULL REFERENCES foods(id),
    quantity DECIMAL(10,2) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units(id),
    is_main_ingredient BOOLEAN DEFAULT FALSE,
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_recipe_ingredients_recipe_id ON recipe_ingredients(recipe_id);
CREATE INDEX idx_recipe_ingredients_food_id ON recipe_ingredients(food_id);
CREATE INDEX idx_recipe_ingredients_main ON recipe_ingredients(is_main_ingredient) WHERE is_main_ingredient = TRUE;
