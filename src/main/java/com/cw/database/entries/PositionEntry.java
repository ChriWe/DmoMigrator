package com.cw.database.entries;

import org.postgis.Geometry;
import org.postgis.Point;
import sun.plugin.util.UIUtil;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by Christoph on 07.12.2015.
 */
public class PositionEntry {

    private final String tableName = "position";

    private String schema;
    private UUID uuid;
    private UUID player_id;
    private int ingame_id;
    private Point coord;
    private double yaw;
    private double pitch;
    private double roll;
    private double dpos;
    private double dyaw;
    private double dpitch;
    private double droll;
    private int game_lfd;
    private int network_time;
    private String message;
    private int player_run;
    private UUID game_id;

    public PositionEntry(String schema, UUID uuid, UUID player_id, int ingame_id, Point coord, double yaw, double pitch, double roll, double dpos, double dyaw, double dpitch, double droll, int game_lfd, int network_time, String message, int player_run, UUID game_id) {
        this.schema = schema;
        this.uuid = uuid;
        this.game_id = game_id;
        this.player_run = player_run;
        this.message = message;
        this.network_time = network_time;
        this.game_lfd = game_lfd;
        this.coord = coord;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.dpos = dpos;
        this.dyaw = dyaw;
        this.dpitch = dpitch;
        this.droll = droll;
        this.ingame_id = ingame_id;
        this.player_id = player_id;
    }

    public String getPreparedSQL() {
        String sql = "INSERT INTO " + schema + "." + tableName + " (pos_id, pos_player_id, pos_ingame_id, pos_coord, pos_yaw, pos_pitch, pos_roll, pos_dpos, pos_dyaw, pos_dpitch, pos_droll, pos_game_lfd, pos_network_time, pos_message, pos_player_run, pos_game_id) " +
                "VALUES " + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        return sql;
    }

    public List getPreparedObjects() {
        return Arrays.asList(uuid, player_id, ingame_id, coord, yaw, pitch, roll, dpos, dyaw, dpitch, droll, game_lfd, network_time, message, player_run, game_id);
    }
}
