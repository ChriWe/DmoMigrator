package com.cw.migrator;

/**
 * Created by Christoph on 07.12.2015.
 */
public class TrjData {

    private final String clientnum;
    private final String x;
    private final String y;
    private final String z;
    private final String yaw;
    private final String pitch;
    private final String roll;
    private final String dpos;
    private final String dyaw;
    private final String dpitch;
    private final String droll;
    private final String timecounter;
    private final String lastupdate;
    private final String msg;

    public TrjData(String clientnum, String x, String y, String z, String yaw, String pitch, String roll, String dpos, String dyaw, String dpitch, String droll, String timecounter, String lastupdate, String msg) {
        this.clientnum = clientnum;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        this.dpos = dpos;
        this.dyaw = dyaw;
        this.dpitch = dpitch;
        this.droll = droll;
        this.timecounter = timecounter;
        this.lastupdate = lastupdate;
        this.msg = msg;
    }

    public String getClientnum() {
        return clientnum;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getZ() {
        return z;
    }

    public String getYaw() {
        return yaw;
    }

    public String getPitch() {
        return pitch;
    }

    public String getRoll() {
        return roll;
    }

    public String getDpos() {
        return dpos;
    }

    public String getDyaw() {
        return dyaw;
    }

    public String getDpitch() {
        return dpitch;
    }

    public String getDroll() {
        return droll;
    }

    public String getTimecounter() {
        return timecounter;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public String getMsg() {
        return msg;
    }
}
