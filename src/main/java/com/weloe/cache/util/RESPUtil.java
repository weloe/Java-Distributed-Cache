package com.weloe.cache.util;

import com.weloe.cache.server.resp.RESPRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * @author weloe
 */
public class RESPUtil {



    /**
     * 发送RESP请求
     * @param host
     * @param port
     * @param args
     * @return
     * @throws IOException
     */
    public static byte[] sendRequest(String host,Integer port, String ... args) throws IOException {
        Socket socket = new Socket(host,port);
        RESPRequest request = new RESPRequest(socket);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        sendRequest(writer,args);

        byte[] param = request.getBytes();

        request.close();
        writer.close();
        socket.close();
        return param;
    }

    /**
     * 发送RESP请求
     * @param writer
     * @param args
     * @throws IOException
     */
    public static void sendRequest(PrintWriter writer, String ... args) throws IOException {
        writer.println("*"+args.length);
        for (String arg : args) {
            writer.println("$"+arg.getBytes(StandardCharsets.UTF_8).length);
            writer.println(arg);
        }
        writer.flush();
    }



    /**
     * 解析RESP协议的字节
     *
     * @return
     */
    public static String parseRESPBytes(InputStream inputStream) {
        byte[] bytes;

        String result = null;
        try {
            while (inputStream.available() == 0) {
            }
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            result = new String(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 解析RESP协议的String
     *
     * @param raw
     * @return
     */
    public static Object parseRESPString(String raw) {
        byte type = raw.getBytes()[0];
        String result = raw.substring(1);
        switch (type) {
            case '+':
                // +ok\r\n
                // 读单行
                return result.replace("\r\n", "");
            case '-':
                // 异常
                // -Error msg\r\n
                throw new RuntimeException(result.replace("\r\n", ""));
            case ':':
                // 数字
                return result.replace("\r\n", "");
            case '$':
                return result.split("\r\n")[1];
            case '*':
                // 多行字符串
                String[] strList = result.substring(result.indexOf("$")).split("\r\n");
                System.out.print("多条批量请求：");
                List<String> list = new LinkedList<>();
                for (int i = 1; i < strList.length; i += 2) {
                    System.out.print(strList[i] + " ");
                    list.add(strList[i]);
                }
                System.out.println();
                return list;
            default:
                throw new RuntimeException("错误的数据格式");
        }
    }
}
