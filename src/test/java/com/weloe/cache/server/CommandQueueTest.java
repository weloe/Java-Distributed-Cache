package com.weloe.cache.server;

import com.weloe.cache.server.parser.CommandQueue;
import com.weloe.cache.server.resp.RESPContext;
import com.weloe.cache.server.vo.CommandVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;

class CommandQueueTest {

    @Test
    void add() throws InterruptedException {
        RESPContext respServer = new RESPContext();
        CommandQueue commandQueue = new CommandQueue();

        commandQueue.add(new RESPContext());
        commandQueue.add(new RESPContext());
        commandQueue.add(new RESPContext());

        RESPContext take = commandQueue.getCommandQueue().take();



    }

    @Test
    void consume() {

    }


}