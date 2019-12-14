package com.demo.netty.t2;

import com.demo.netty.t1.Server;

import java.awt.*;

public class ServerFrame extends Frame {

    public static ServerFrame INSTANCE = new ServerFrame();

    Button btnStart = new Button("start");
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();
    Server server = new Server();

    public ServerFrame() {

    }


}
