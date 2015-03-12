package com.socialmap.server.gui;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by yy on 3/9/15.
 */
public class LocalServerThread extends Thread {
    @Override
    public void run() {
        try {
            org.eclipse.jetty.util.log.Log.setLog(new org.eclipse.jetty.util.log.Slf4jLog());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Server server = new Server(8000);
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar(getClass().getResource("/sample.war").getPath());
        server.setHandler(webapp);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
