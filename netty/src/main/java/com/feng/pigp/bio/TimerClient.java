package com.feng.pigp.bio;

import com.feng.pigp.util.LogUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author feng
 * @date 2019/4/28 19:53
 * @since 1.0
 */
public class TimerClient {

    public void run(){

        int port = 9090;
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("localhost", port));

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bw.write("query time");
            bw.newLine();
            bw.flush();
            LogUtil.newInstanace().info("request send ");

            String response = br.readLine();
            LogUtil.newInstanace().info("response is : {}", response);
        } catch (IOException e) {
            LogUtil.newInstanace().error("timer client connect exception : {}", port, e);
        }
    }

    public static void main(String[] args) {

        TimerClient timerClient = new TimerClient();
        timerClient.run();
    }
}