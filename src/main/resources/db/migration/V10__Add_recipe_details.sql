-- V10__Add_recipe_details.sql
-- Add new fields to recipes table: difficulty, prepTime, cookTime, servings, tags

-- Add difficulty column (EASY, MEDIUM, HARD)
ALTER TABLE recipes ADD COLUMN difficulty VARCHAR(20) DEFAULT 'MEDIUM';

-- Add prep_time column (in minutes)
ALTER TABLE recipes ADD COLUMN prep_time INTEGER DEFAULT 0;

-- Add cook_time column (in minutes)
ALTER TABLE recipes ADD COLUMN cook_time INTEGER DEFAULT 0;

-- Add servings column
ALTER TABLE recipes ADD COLUMN servings INTEGER DEFAULT 2;

-- Add tags column (stored as JSON array)
ALTER TABLE recipes ADD COLUMN tags TEXT[];

-- Create index for difficulty
CREATE INDEX idx_recipes_difficulty ON recipes(difficulty);

-- Create index for tags using GIN
CREATE INDEX idx_recipes_tags ON recipes USING GIN(tags);
