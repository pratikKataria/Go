package com.tricky_tweaks.go.DataModel;

public class GatePassData {
    private String gp_id;
    private String s_id;
    private String gp_from;
    private String gp_to;
    private String gp_reason;
    private String gp_s_count;

    public GatePassData() {}

    public GatePassData(String gp_id, String s_id, String gp_from, String gp_to, String gp_reason, String gp_s_count, String gp_moderator, String gp_duration, String gp_time, int gp_status) {
        this.gp_id = gp_id;
        this.s_id = s_id;
        this.gp_from = gp_from;
        this.gp_to = gp_to;
        this.gp_reason = gp_reason;
        this.gp_s_count = gp_s_count;
        this.gp_moderator = gp_moderator;
        this.gp_duration = gp_duration;
        this.gp_time = gp_time;
        this.gp_status = gp_status;
    }

    public String getGp_id() {
        return gp_id;
    }

    public void setGp_id(String gp_id) {
        this.gp_id = gp_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getGp_from() {
        return gp_from;
    }

    public void setGp_from(String gp_from) {
        this.gp_from = gp_from;
    }

    public String getGp_to() {
        return gp_to;
    }

    public void setGp_to(String gp_to) {
        this.gp_to = gp_to;
    }

    public String getGp_reason() {
        return gp_reason;
    }

    public void setGp_reason(String gp_reason) {
        this.gp_reason = gp_reason;
    }

    public String getGp_s_count() {
        return gp_s_count;
    }

    public void setGp_s_count(String gp_s_count) {
        this.gp_s_count = gp_s_count;
    }

    public String getGp_moderator() {
        return gp_moderator;
    }

    public void setGp_moderator(String gp_moderator) {
        this.gp_moderator = gp_moderator;
    }

    public String getGp_duration() {
        return gp_duration;
    }

    public void setGp_duration(String gp_duration) {
        this.gp_duration = gp_duration;
    }

    public String getGp_time() {
        return gp_time;
    }

    public void setGp_time(String gp_time) {
        this.gp_time = gp_time;
    }

    public int getGp_status() {
        return gp_status;
    }

    public void setGp_status(int gp_status) {
        this.gp_status = gp_status;
    }

    private String gp_moderator;
    private String gp_duration;
    private String gp_time;
    private int gp_status;

}
