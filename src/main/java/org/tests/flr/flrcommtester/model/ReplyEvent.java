package org.tests.flr.flrcommtester.model;

public class ReplyEvent {
    
    private boolean heartbeat;
    private byte[] payload;

    public byte[] getPayload() {
        return payload;
    }
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
    public boolean isHeartbeat() {
        return heartbeat;
    }
    public void setHeartbeat(boolean heartbeat) {
        this.heartbeat = heartbeat;
    }

    
}