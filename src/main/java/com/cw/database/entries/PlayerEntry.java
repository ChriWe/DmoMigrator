package com.cw.database.entries;

/**
 * Created by Christoph on 07.12.2015.
 */
public class PlayerEntry {

    private final String tableName = "player";

    private String schema;
    private String id;
    private String name;
    private String game_id;
    private String ingame_id;
    private String runs;

    public PlayerEntry(String schema, String id, String name, String game_id, String ingame_id, String runs) {
        this.schema = schema;
        this.id = id;
        this.name = name;
        this.game_id = game_id;
        this.ingame_id = ingame_id;
        this.runs = runs;
    }

    public String toSQL() {
        String sql = "INSERT INTO " + schema + "." + tableName + " (player_id, player_name, player_game_id, player_ingame_id, player_runs) " +
                "VALUES " + " (" + id + ",'"+ name + "'," + game_id + "," + ingame_id + "," + runs + ")";

        return sql;
    }
}
