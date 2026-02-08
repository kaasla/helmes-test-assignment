CREATE TABLE user_selections (
    id             BIGINT       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    session_id     VARCHAR(255) NOT NULL,
    name           VARCHAR(255) NOT NULL,
    agree_to_terms BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_user_selections_session_id ON user_selections(session_id);

CREATE TABLE user_selection_sectors (
    user_selection_id BIGINT NOT NULL REFERENCES user_selections(id) ON DELETE CASCADE,
    sector_id         BIGINT NOT NULL REFERENCES sectors(id) ON DELETE CASCADE,
    PRIMARY KEY (user_selection_id, sector_id)
);

CREATE INDEX idx_user_selection_sectors_sector_id ON user_selection_sectors(sector_id);