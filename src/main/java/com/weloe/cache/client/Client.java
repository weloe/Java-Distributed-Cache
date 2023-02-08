package com.weloe.cache.client;

import com.weloe.cache.util.RESPUtil;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    static Socket socket;
    static PrintWriter writer;
    static BufferedReader reader;
    static InputStream inputStream;

    static String host = "127.0.0.1";
    static int port = 8081;

    public static void main(String[] args) {

        try {
            // 建立连接
            socket = new Socket(host,port);
            // 获取输出输入流
            // 字符输出流，可以直接按行输出
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            // 字节流读
            //reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
            inputStream = socket.getInputStream();

            while (true) {
                Scanner reader = new Scanner(System.in);
                if (reader.hasNextLine()) {
                    String s = reader.nextLine();
                    if("exit".equals(s)){
                        System.out.println("exit cli");
                        return;
                    }
                    if (!s.isEmpty()) {
                        Object obj;
                        // 操作命令
                        sendRequest(s.split(" "));
                        // 响应
                        obj = inputStreamHandleByteResponse();
                        System.out.println(obj);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放连接
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void sendRequest(String ... args) {

        writer.println("*"+args.length);
        for (String arg : args) {
            writer.println("$"+arg.getBytes(StandardCharsets.UTF_8).length);
            writer.println(arg);
        }

        writer.flush();

    }


    public static Object inputStreamHandleByteResponse() {
        String result = RESPUtil.parseRESPBytes(inputStream);
        return RESPUtil.parseRESPString(result);
    }


}
