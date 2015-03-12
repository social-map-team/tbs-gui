package com.socialmap.server.share;

/**
 * Created by yy on 3/9/15.
 */
public class TbsInfo {
    private int apiPort;
    private String realm;
    private int runningTime;

    public int getApiPort() {
        return apiPort;
    }

    public void setApiPort(int apiPort) {
        this.apiPort = apiPort;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }
}
