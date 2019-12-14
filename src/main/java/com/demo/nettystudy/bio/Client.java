package com.demo.nettystudy.bio;

import java.io.IOException;
import java.net.Socket;

/**
 * bio
 */
public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);
        socket.getOutputStream().write("HelloServer".getBytes());
        socket.getOutputStream().flush();

        System.out.println("write over,waiting for msg back...");
        byte[] bytes = new byte[1024];
        int len = socket.getInputStream().read(bytes);
        System.out.println(new String(bytes, 0, len));
        socket.close();
    }

}
