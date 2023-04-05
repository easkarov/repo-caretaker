-- liquibase formatted sql

-- changeset EmiAsk:create_chat_table
CREATE TABLE IF NOT EXISTS chat
(
    id INTEGER PRIMARY KEY
);


-- changeset EmiAsk:create_link_table
CREATE TABLE IF NOT EXISTS link
(
    id SERIAL PRIMARY KEY,
    url TEXT NOT NULL UNIQUE,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);


-- changeset EmiAsk:create_chat_link_table
CREATE TABLE IF NOT EXISTS chat_link
(
    chat_id INTEGER,
    link_id INTEGER,
    PRIMARY KEY (chat_id, link_id),
    FOREIGN KEY (chat_id) REFERENCES chat(id),
    FOREIGN KEY (link_id) REFERENCES link(id)
);
