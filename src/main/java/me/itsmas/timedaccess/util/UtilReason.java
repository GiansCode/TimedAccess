package me.itsmas.timedaccess.util;

public class UtilReason {

    private long timestamp;
    private long timeAdded;
    private String reason;
    private String who;

    public UtilReason(long timestamp, long timeAdded, String reason, String who) {
        this.timestamp = timestamp;
        this.timeAdded = timeAdded;
        this.reason = reason;
        this.who = who;
    }

    public static UtilReason parseReason(String reasonObj) {
        String[] reason_data = reasonObj.split(",");
        long timestamp = Long.parseLong(reason_data[0]);
        long timeAdd = Long.parseLong(reason_data[1]);
        String reason_msg = reason_data[2];
        String who = reason_data[3];
        UtilReason reason = new UtilReason(timestamp, timeAdd, reason_msg, who);
        return reason;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public String getReason() {
        return reason;
    }

    public String getWho() {
        return who;
    }

    public String createReasonString() {
        return timestamp+","+timeAdded+","+reason+","+who;
    }

}
