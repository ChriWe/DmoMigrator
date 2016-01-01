-- Table: import."position"

DROP TABLE import."position";

CREATE TABLE import."position"
(
  pos_id uuid NOT NULL,
  pos_player_id uuid,
  pos_ingame_id bigint,
  pos_coord geometry(PointZ),
  pos_yaw double precision,
  pos_pitch double precision,
  pos_roll double precision,
  pos_dpos double precision,
  pos_dyaw double precision,
  pos_dpitch double precision,
  pos_droll double precision,
  pos_game_lfd bigint,
  pos_network_time bigint,
  pos_message character varying(200),
  pos_player_run bigint,
  pos_game_id uuid,
  CONSTRAINT import_pos_pkey PRIMARY KEY (pos_id),
  CONSTRAINT game_pos_fk FOREIGN KEY (pos_game_id)
      REFERENCES import.game (game_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE import."position"
  OWNER TO postgres;