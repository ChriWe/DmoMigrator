-- Table: import.player

-- DROP TABLE import.player;

CREATE TABLE player
(
  player_id uuid NOT NULL,
  player_name character varying(200),
  player_game_id uuid,
  player_ingame_id bigint,
  player_runs bigint,
  CONSTRAINT player_pkey PRIMARY KEY (player_id),
  CONSTRAINT player_game_fk FOREIGN KEY (player_game_id)
      REFERENCES import.game (game_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE player
  OWNER TO postgres;