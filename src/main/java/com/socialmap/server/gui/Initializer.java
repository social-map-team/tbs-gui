package com.socialmap.server.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by yy on 3/9/15.
 */
@Component
public class Initializer {
    private static final Logger log = LogManager.getLogger();

    @Autowired
    public Initializer(ApplicationContext context) {
        log.info("初始化。。。");

        // 启动 sos 监听线程
        App.sosThread = new SosThread();
        App.sosThread.start();

        new File("where-are-you.txt");


        Environment env = context.getEnvironment();
        // 读取配置
        App.SOS_PORT = Integer.parseInt(env.getProperty("sos.port"));
        App.SOS_TIMEOUT = Integer.parseInt(env.getProperty("sos.timeout")) * 1000;
        String serverType = env.getProperty("server.type");
        if (serverType.equalsIgnoreCase("local")){
            App.SERVER_TYPE = App.ServerType.LOCAL;
        } else if (serverType.equalsIgnoreCase("remote")) {
            App.SERVER_TYPE = App.ServerType.REMOTE;
        }

        // 检查网络
    }
}
