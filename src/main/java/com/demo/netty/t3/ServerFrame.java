package com.demo.netty.t3;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends Frame {

    public static ServerFrame INSTANCE = new ServerFrame();

    Button btnStart = new Button("start");
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();
    Server server = new Server();

    public ServerFrame() {
        this.setSize(1600, 600);
        this.setLocation(300, 30);
        this.add(btnStart, BorderLayout.NORTH);
        Panel panel = new Panel(new GridLayout(1, 2));
        panel.add(taLeft);
        panel.add(taRight);
        this.add(panel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        /*btnStart.addActionListener((event) -> {
            new Thread(() -> {
                server.startServer();
            });
        });*/

    }

    public void updateServerMsg(String str) {
        this.taLeft.setText(taLeft.getText() + System.getProperty("line.separator") + str);
    }

    public void updateClientMsg(String str) {
        this.taRight.setText(taRight.getText() + System.getProperty("line.separator") + str);
    }

    public static void main(String[] args) {
        ServerFrame.INSTANCE.setVisible(true);
        ServerFrame.INSTANCE.server.startServer();
    }


}
