-- liquibase formatted sql

-- changeset EmiAsk:create_chat_table
CREATE TABLE IF NOT EXISTS chat
(
    id BIGINT PRIMARY KEY
);


-- changeset EmiAsk:create_link_table
CREATE TABLE IF NOT EXISTS link
(
    id BIGSERIAL PRIMARY KEY,
    url TEXT NOT NULL UNIQUE,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);


-- changeset EmiAsk:create_chat_link_table
CREATE TABLE IF NOT EXISTS chat_link
(
    chat_id BIGINT,
    link_id BIGINT,
    PRIMARY KEY (chat_id, link_id),
    FOREIGN KEY (chat_id) REFERENCES chat(id),
    FOREIGN KEY (link_id) REFERENCES link(id)
);
