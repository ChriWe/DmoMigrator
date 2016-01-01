package com.cw.database.entries;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Christoph on 07.12.2015.
 */
public class PlayerEntry {

    private final String tableName = "player";
    private final String schema;

    private final UUID uuid;
    private final String name;
    private final UUID game_id;
    private final int ingame_id;
    private final String runs;

    public PlayerEntry(String schema, UUID uuid, String name, UUID game_id, int ingame_id, String runs) {
        this.schema = schema;
        this.uuid = uuid;
        this.name = name;
        this.game_id = game_id;
        this.ingame_id = ingame_id;
        this.runs = runs;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getPreparedSQL() {

        String sql = "INSERT INTO " + schema + "." + tableName + " (player_id, player_name, player_game_id, player_ingame_id, player_runs) " +
                "VALUES " + " (?, ?, ?, ?,?)";

        return sql;
    }

    public List getPreparedObjects() {
        return Arrays.asList(uuid, name, game_id, ingame_id, runs);
    }

}
