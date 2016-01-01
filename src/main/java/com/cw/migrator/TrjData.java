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
    private final String callingMethod;

    public TrjData(String clientnum, String x, String y, String z, String yaw, String pitch, String roll, String dpos, String dyaw, String dpitch, String droll, String timecounter, String lastupdate, String callingMethod) {
        this.clientnum = clientnum.trim();
        this.x = x.trim();
        this.y = y.trim();
        this.z = z.trim();
        this.yaw = yaw.trim();
        this.pitch = pitch.trim();
        this.roll = roll.trim();
        this.dpos = dpos.trim();
        this.dyaw = dyaw.trim();
        this.dpitch = dpitch.trim();
        this.droll = droll.trim();
        this.timecounter = timecounter.trim();
        this.lastupdate = lastupdate.trim();
        this.callingMethod = callingMethod.trim();
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

    public String getCallingMethod() {
        return callingMethod;
    }
}
