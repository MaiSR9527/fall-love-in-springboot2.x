package com.msr.better.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 20:50:59
 */
@Component
public class BlackListProperties {
    /**
     * 检测恶意IP，多少秒被出现多少次请求
     */
    @Value("${blackList.ip.times}")
    private int ipBlackTimes;

    /**
     * 检测恶意IP，多少秒被出现多少次请求
     */
    @Value("${blackList.ip.interval}")
    private int ipBlackSeconds;

    /**
     * 检测恶意用户，多少秒被出现多少次请求
     */
    @Value("${blackList.user.times}")
    private int userBlackTimes;

    /**
     * 检测恶意用户，多少秒被出现多少次请求
     */
    @Value("${blackList.user.interval}")
    private int userBlackSeconds;

    public int getIpBlackTimes() {
        return ipBlackTimes;
    }

    public void setIpBlackTimes(int ipBlackTimes) {
        this.ipBlackTimes = ipBlackTimes;
    }

    public int getIpBlackSeconds() {
        return ipBlackSeconds;
    }

    public void setIpBlackSeconds(int ipBlackSeconds) {
        this.ipBlackSeconds = ipBlackSeconds;
    }

    public int getUserBlackTimes() {
        return userBlackTimes;
    }

    public void setUserBlackTimes(int userBlackTimes) {
        this.userBlackTimes = userBlackTimes;
    }

    public int getUserBlackSeconds() {
        return userBlackSeconds;
    }

    public void setUserBlackSeconds(int userBlackSeconds) {
        this.userBlackSeconds = userBlackSeconds;
    }

}
