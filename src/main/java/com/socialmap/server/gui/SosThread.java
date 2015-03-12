package com.socialmap.server.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmap.server.share.SosRequest;
import com.socialmap.server.share.SosTarget;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by yy on 3/9/15.
 */
public class SosThread extends Thread {

    private boolean ok;

    private Stage stage;

    private void handle(final SosRequest request) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/sos.fxml"));

                    TitledPane tp1 = (TitledPane) root.lookup("#_info");
                    Node tp = tp1.getContent();

                    Label _realname = (Label) tp.lookup("#_realname");
                    _realname.setText(request.getCaller().getRealname());

                    Label _usernameId = (Label) tp.lookup("#_usernameId");
                    _usernameId.setText(request.getCaller().getUsername() + "/" + request.getCaller().getId());

                    Label _position = (Label) tp.lookup("#_position");
                    _position.setText(request.getCaller().getPosition());

                    Label _reason = (Label) tp.lookup("#_reason");
                    _reason.setText(request.getReason());

                    Label _phone = (Label) tp.lookup("#_phone");
                    _phone.setText(request.getCaller().getPhone());

                    ListView _targets = (ListView) root.lookup("#_targets");

                    ObservableList<String> items = FXCollections.observableArrayList();

                    for (SosTarget t : request.getTargets()) {
                        String s = t.getName() + "（" + t.getRelationship() + "）：  " + t.getPhone();
                        items.add(s);
                    }

                    _targets.setItems(items);

                    stage = new Stage();

                    Button _finish = (Button) root.lookup("#_finish");
                    _finish.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            ok = true;
                            synchronized (lock) {
                                lock.notifyAll();
                            }
                            stage.close();
                        }
                    });


                    stage.setScene(new Scene(root));
                    stage.setTitle("紧急求助");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private ServerSocket server;
    private boolean shutdown = false;

    private static final class Lock {
    }

    private final Object lock = new Lock();

    private void closeStage(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.close();
            }
        });
    }

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            server = new ServerSocket(App.SOS_PORT);
            while (true) {
                Socket socket = server.accept();
                try {
                    String jsonStr = new DataInputStream(socket.getInputStream()).readUTF();
                    System.out.println(jsonStr);
                    SosRequest request = mapper.readValue(jsonStr, SosRequest.class);
                    ok = false;
                    handle(request);
                    synchronized (lock) {
                        lock.wait(App.SOS_TIMEOUT);
                    }
                    closeStage();
                    if (ok) {
                        new DataOutputStream(socket.getOutputStream()).writeUTF("已处理");
                    } else {
                        new DataOutputStream(socket.getOutputStream()).writeUTF("未处理");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }
            }
        } catch (Exception e) {
            if (!shutdown) e.printStackTrace();
        }
    }

    public void stopIt() {
        try {
            shutdown = true;
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
