package com.cw.database.entries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Christoph on 07.12.2015.
 */
public class GameEntry {

    private final String tableName = "game";
    private final String schema;

    private final UUID uuid;
    private final String filename;
    private final String mapname;
    private final String maxplayers;

    public GameEntry(String schema, UUID uuid, String filename, String mapname, String maxplayers) {
        this.schema = schema;
        this.uuid = uuid;
        this.maxplayers = maxplayers;
        this.mapname = mapname;
        this.filename = filename;
    }

    public String getPreparedSQL() {

        String sql = "INSERT INTO " + schema + "." + tableName + " (game_id, game_filename, game_mapname, game_maxplayers) " +
              "VALUES " + " (?, ?, ?, ?)";

        return sql;
    }

    public List getPreparedObjects() {
        return Arrays.asList(uuid, filename, mapname, maxplayers);
    }
}
