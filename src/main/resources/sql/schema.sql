CREATE TABLE IF NOT EXISTS articles (
    id VARCHAR(128)  NOT NULL DEFAULT RANDOM_UUID() PRiMARY KEY,
    head VARCHAR(128) NOT NULL,
    description VARCHAR(128) NOT NULL
);