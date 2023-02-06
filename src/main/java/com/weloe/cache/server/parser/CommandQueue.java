package com.weloe.cache.server.parser;

import com.weloe.cache.server.resp.RESPContext;
import com.weloe.cache.server.vo.CommandVO;

import java.util.concurrent.*;

/**
 * @author weloe
 */
public class CommandQueue {

    private static PriorityBlockingQueue<RESPContext> commandQueue = new PriorityBlockingQueue<>(5, (o1, o2) -> {
        if (o1.getStartTime().isBefore(o2.getStartTime())) {
            return -1;
        }
        return 1;
    });

    public boolean add(RESPContext context){
        return commandQueue.add(context);
    }


    public void consume() {
        new Thread(() -> {
            System.out.println("服务端等待接收命令...");
            while (true) {
                RESPContext respContext = null;
                try {
                    respContext = commandQueue.take();
                } catch (InterruptedException e) {
                    respContext.error(e.getMessage());
                    e.printStackTrace();
                    continue;
                }

                System.out.println("执行命令"+respContext.getRoute().getNode().getPattern());
                // 执行命令
                Object handle = respContext.getRoute().handle(respContext);
                System.out.println(handle);
                if(handle == null){
                    respContext.ok("nil");
                }else if(handle.equals("")){
                    respContext.ok();
                }else {
                    respContext.ok(handle.toString());
                }
            }
        }).start();


    }

    public PriorityBlockingQueue<RESPContext> getCommandQueue() {
        return commandQueue;
    }
}
