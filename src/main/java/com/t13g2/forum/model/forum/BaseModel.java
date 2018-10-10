package com.t13g2.forum.model.forum;

import java.util.Date;

public class BaseModel {
    protected int id;
    protected Date created = new Date();
    protected Date modified;
    protected int createdByUserId;
    protected int modifiedByUserId;

    public int getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public int getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public void setModifiedByUserId(int modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }
}
