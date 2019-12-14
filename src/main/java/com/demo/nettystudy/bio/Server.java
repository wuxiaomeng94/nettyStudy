package com.demo.nettystudy.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress("127.0.0.1", 8888));
        while (true) {
            Socket socket = ss.accept();//阻塞方法

            new Thread(() -> {
               handle(socket);
            }).start();
        }
    }

    public static void handle(Socket socket) {

        try {
            byte[] bytes = new byte[1024];
            int len = socket.getInputStream().read(bytes);
            System.out.println(new String(bytes, 0, len));

            socket.getOutputStream().write(bytes, 0, len);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
