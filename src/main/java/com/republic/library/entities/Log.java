package com.republic.library.entities;

import java.util.Date;

public final class Log {
    private final int id;
    private final String action;
    private final int initiatorId;
    private final int targetId;
    private final String newValue;
    private final String oldValue;
    private final Date createdAt;

    public Log(int id, String action, int initiatorId, int targetId, String newValue, String oldValue, Date createdAt) {
        this.id = id;
        this.action = action;
        this.initiatorId = initiatorId;
        this.targetId = targetId;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public int getInitiatorId() {
        return initiatorId;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
