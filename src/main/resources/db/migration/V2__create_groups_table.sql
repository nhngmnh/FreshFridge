-- V2: Create groups and group_members tables
-- Migration for Group domain

-- Groups table
CREATE TABLE groups (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    admin_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_groups_admin FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_groups_admin UNIQUE (admin_id)
);

-- Group members table
CREATE TABLE group_members (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_group_members_group FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE,
    CONSTRAINT fk_group_members_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_group_members_user UNIQUE (user_id)
);

-- Indexes for performance
CREATE INDEX idx_groups_admin_id ON groups(admin_id);
CREATE INDEX idx_group_members_group_id ON group_members(group_id);
CREATE INDEX idx_group_members_user_id ON group_members(user_id);

-- Comments
COMMENT ON TABLE groups IS 'Family/household groups';
COMMENT ON COLUMN groups.name IS 'Name of the group (family name)';
COMMENT ON COLUMN groups.admin_id IS 'User ID of the group administrator';

COMMENT ON TABLE group_members IS 'Members belonging to groups';
COMMENT ON COLUMN group_members.group_id IS 'Reference to the group';
COMMENT ON COLUMN group_members.user_id IS 'Reference to the user member';
COMMENT ON COLUMN group_members.joined_at IS 'When the member joined the group';
