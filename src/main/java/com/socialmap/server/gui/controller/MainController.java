package com.socialmap.server.gui.controller;

import com.socialmap.server.gui.App;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

/**
 * Created by yy on 3/9/15.
 */
public class MainController implements Initializable {
    private static final Logger log = LogManager.getLogger();
    @FXML
    TextField _ip;
    @FXML
    TextField _port;
    @FXML
    Button _remote;
    @FXML
    Button _start;
    @FXML
    Button _stop;
    @FXML
    Button _db;
    @FXML
    TextArea _log;

    @FXML
    public void clearlog(){
        _log.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String ip = InetAddress.getLocalHost().toString();
            _ip.setText(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        _remote.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage stage = new Stage();
                    App.stages.put("connect", stage);
                    Parent root = FXMLLoader.load(getClass().getResource("/connect.fxml"));
                    stage.setScene(new Scene(root));
                    stage.setTitle("连接远程服务器");

                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(
                            ((Node)event.getSource()).getScene().getWindow() );

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
