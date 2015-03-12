package com.socialmap.server.gui;

import com.socialmap.server.gui.config.AppConfig;
import com.socialmap.server.share.ConsoleInfo;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yy on 3/8/15.
 */
public class App extends Application {

    public static class ServerType {
        public static final int LOCAL = 1;
        public static final int REMOTE = 2;
    }

    private static final Logger log = LogManager.getLogger();
    public static final String CONFIGURATION_FILENAME = "console.properties";
    public static int SOS_PORT = 9999;
    public static int SOS_TIMEOUT = 1800000; // 30 minutes
    public static int SERVER_TYPE = ServerType.REMOTE;
    public static Map<String, Stage> stages = new HashMap<>();
    public static ConsoleInfo info = new ConsoleInfo();
    public static TextArea logArea;
    public static SosThread sosThread;

    public static TextField _ip;
    public static TextField _port;

    private static final String SERVER_NOT_CONNECTED = "<服务器未连接>";


    @Override
    public void start(Stage primaryStage) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();
        stages.put("main", primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        logArea = (TextArea) root.lookup("#_log");

        Node node = ((TitledPane) root.lookup("#_info")).getContent();
        _ip = (TextField) node.lookup(INFO_IP);
        _port = (TextField) node.lookup(INFO_PORT);

        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                App.sosThread.stopIt();
                Iterator i = stages.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry e = (Map.Entry) i.next();
                    ((Stage) e.getValue()).close();
                }
            }
        });
        primaryStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                switch (App.SERVER_TYPE) {
                    case ServerType.LOCAL:

                        break;
                    case ServerType.REMOTE:
                        App._ip.setText(SERVER_NOT_CONNECTED);
                        App._ip.setEditable(false);
                        App._port.setText(SERVER_NOT_CONNECTED);
                        App._port.setEditable(false);
                        break;
                }
            }
        });
        primaryStage.show();

    }

    public static void log(String message) {
        String content = logArea.getText();
        if (content.isEmpty()) {
            logArea.setText(message);
        } else {
            logArea.setText(content + "\n" + message);
        }
    }

    public static final String INFO_IP = "#_ip";
    public static final String INFO_PORT = "#_port";

    public static void main(String[] args) {
        log.info("程序启动");
        launch(args);
    }
}
