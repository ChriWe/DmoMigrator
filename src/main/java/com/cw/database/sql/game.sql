-- Table: import.game

-- DROP TABLE import.game;

CREATE TABLE import.game
(
  game_id uuid NOT NULL,
  game_filename character varying(200) NOT NULL,
  game_mapname character varying(100) NOT NULL,
  game_maxplayers character varying(10) NOT NULL,
  CONSTRAINT game_pkey PRIMARY KEY (game_id),
  CONSTRAINT game_game_filename_check CHECK (game_filename::text <> ''::text),
  CONSTRAINT game_game_mapname_check CHECK (game_mapname::text <> ''::text),
  CONSTRAINT game_game_maxplayers_check CHECK (game_maxplayers::text <> ''::text)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE import.game
  OWNER TO postgres;