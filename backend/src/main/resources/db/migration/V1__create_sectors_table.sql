CREATE TABLE sectors (
    id   BIGINT       PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT  REFERENCES sectors(id) ON DELETE CASCADE
);

CREATE INDEX idx_sectors_parent_id ON sectors(parent_id);