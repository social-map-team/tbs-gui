package com.socialmap.server.share;

import com.socialmap.server.gui.App;

/**
 * Created by yy on 3/8/15.
 */
public class ConsoleInfo {
    private int sosPort;

    public ConsoleInfo(){
        sosPort = App.SOS_PORT;
    }

    public int getSosPort() {
        return sosPort;
    }

    public void setSosPort(int sosPort) {
        this.sosPort = sosPort;
    }
}
