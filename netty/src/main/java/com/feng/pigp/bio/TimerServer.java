package com.feng.pigp.bio;

import com.feng.pigp.util.LogUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author feng
 * @date 2019/4/28 19:10
 * @since 1.0
 */
public class TimerServer {

    public static final ThreadPoolExecutor excutor= new ThreadPoolExecutor(4, 10, 5000, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(100));

    public void run(){

        int port = 9090;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true) {
                Socket socket = serverSocket.accept();
                excutor.execute(new TimerHandler(socket));
            }
        } catch (IOException e) {
            LogUtil.getLogger().error("bind port exception : {}", port, e);
        }
    }

    public static void main(String[] args) {
        TimerServer timerServer = new TimerServer();
        timerServer.run();
    }

    public static class TimerHandler implements Runnable{

        private Socket socket;

        public TimerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            LogUtil.getLogger().info("socket has connected");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String request = br.readLine();
                LogUtil.getLogger().info("request={}", request);
                if("query time".equals(request)){
                    bw.write("current time : " + System.currentTimeMillis());
                }else{
                    bw.write("bad request");
                }
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                LogUtil.getLogger().error("socket read write exception", e);
            }
        }
    }
}