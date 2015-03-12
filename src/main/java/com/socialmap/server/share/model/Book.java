package com.socialmap.server.share.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by yy on 3/9/15.
 */
@Entity
public class Book {
    private String name;

    @Id
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
