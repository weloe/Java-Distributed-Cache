package com.weloe.cache.server.resp;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author weloe
 */
public class RESPResponse {
    private PrintWriter writer;

    public RESPResponse(Socket socket) throws IOException {
        // 字符输出流，可以直接按行输出
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    public void ok() {
        writer.println(RESPStatus.OK.getMsg());
        writer.flush();
    }

    public void ok(Integer arg) {
        writer.println(":" + arg);
        writer.flush();
    }

    public void ok(String arg) {
        writer.println("$" + arg.getBytes(StandardCharsets.UTF_8).length);
        writer.println(arg);
        writer.flush();
    }

    public void ok(String... args) {
        writer.println("*" + args.length);
        for (String arg : args) {
            writer.println("$" + arg.getBytes(StandardCharsets.UTF_8).length);
            writer.println(arg);
        }
        writer.flush();
    }

    public void error(String msg) {
        writer.println(RESPStatus.ERROR.getMsg() + " " + msg);
        writer.flush();
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }

    public PrintWriter getWriter() {
        return writer;
    }


}
