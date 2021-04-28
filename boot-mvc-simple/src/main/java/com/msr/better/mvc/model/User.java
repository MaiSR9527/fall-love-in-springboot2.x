package com.msr.better.mvc.model;

import java.io.Serializable;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-28 23:36
 **/
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
