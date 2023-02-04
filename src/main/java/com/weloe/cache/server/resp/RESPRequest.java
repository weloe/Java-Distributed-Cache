package com.weloe.cache.server.resp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * @author weloe
 */
public class RESPRequest {
    private InputStream inputStream;

    public RESPRequest(Socket socket) throws IOException {
        this.inputStream = socket.getInputStream();
    }

    /**
     * 解析RESP协议的字节
     *
     * @return
     */
    public byte[] getBytes() {
        byte[] bytes = null;

        try {
            while (inputStream.available() == 0) {
            }
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * 解析RESP协议的字节
     *
     * @return
     */
    public String parseRESPBytes() {
        byte[] bytes;
        byte[] buf = new byte[1];
        String result = null;
        try {
            System.out.println("等待数据传输");
//            try {
//                // 读不到阻塞
//                inputStream.read(buf);
//            } catch (IOException e) {
//                return null;
//            }
//            result = new String(buf) + new String(bytes);
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
    public Object parseRESPString(String raw) {
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
                return result.substring(1).replace("\r\n", "");
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

    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }


    public InputStream getInputStream() {
        return inputStream;
    }
}
