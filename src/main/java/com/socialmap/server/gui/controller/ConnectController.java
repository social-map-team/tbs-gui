package com.socialmap.server.gui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmap.server.gui.App;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by yy on 3/9/15.
 */
public class ConnectController implements Initializable {

    @FXML
    TextField _host;
    @FXML
    TextField _port;
    @FXML
    Button _connect;
    @FXML
    Button _cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StringWriter writer = new StringWriter();
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.writeValue(writer, App.info);
                            String json = writer.toString();
                            String host = _host.getText();
                            int port = Integer.parseInt(_port.getText());
                            Socket socket = new Socket(host, port);
                            new DataOutputStream(socket.getOutputStream()).writeUTF(json);
                            String response = new DataInputStream(socket.getInputStream()).readUTF();
                            if (response.equals("服务器已经知晓控制台相关信息")) {
                                App.log("远程服务器（" + host + ":" + port + "）连接成功");
                            }

                            final String ipStr = socket.getInetAddress().getHostAddress();
                            final String portStr = socket.getPort() + "";
                            socket.close();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    App._ip.setText(ipStr);
                                    App._port.setText(portStr);
                                }
                            });
                        } catch (IOException e) {
                            App.log("服务连接失败\n" + e.toString());
                        }
                    }
                }).start();
                App.stages.get("connect").close();
                App.stages.remove("connect");
            }
        });
        _cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                App.stages.get("connect").close();
                App.stages.remove("connect");
            }
        });
    }
}
