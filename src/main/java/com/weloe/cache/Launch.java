package com.weloe.cache;


import com.weloe.cache.cachemanager.GroupManager;
import com.weloe.cache.server.command.ServiceFactory;
import com.weloe.cache.server.command.config.ConfigService;
import com.weloe.cache.server.command.delete.DeleteService;
import com.weloe.cache.server.command.expire.ExpireService;
import com.weloe.cache.server.command.group.GroupService;
import com.weloe.cache.server.command.str.StringService;
import com.weloe.cache.server.parser.CommandQueue;
import com.weloe.cache.server.parser.Router;
import com.weloe.cache.server.resp.RESPContext;
import com.weloe.cache.server.resp.RESPRequest;
import com.weloe.cache.server.resp.RESPResponse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author weloe
 */
public class Launch {
    private static final ExecutorService poolExecutor = new ThreadPoolExecutor(2, 5,
            30L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10));

    private static final GroupManager groupManager = GroupManager.getInstance();

    private static final ServiceFactory factory = new ServiceFactory();

    private static final Router router = new Router();

    static {
        ConfigService config = factory.getBean("config");
        StringService str = factory.getBean("str");
        DeleteService delete = factory.getBean("delete");
        ExpireService expire = factory.getBean("expire");
        GroupService groupService = factory.getBean("group");

        router.addRoute("group add :name", c -> groupService.add(c.getParam("name")));

        router.addRoute("config set maxByteSize :group :size", c -> config.setMaxSize(c.getGroup(),c.getParam("size")))
              .addRoute("config get maxByteSize :group", c -> config.getMaxSize(c.getGroup()))
              .addRoute("config get normalSize :group", c -> config.getNormalSize(c.getGroup()))
              .addRoute("config set maxNum :group :num", c -> config.setMaxNum(c.getGroup(),c.getParam("num")));


        router.addRoute("expire :group :k :n", c -> expire.expire(c.getGroup(),c.getParam("k"),c.getParam("n")))
              .addRoute("ttl :group :k", c -> expire.ttl(c.getGroup(),c.getParam("k")));

        router.addRoute("delete :group :k", c -> delete.delete(c.getGroup(),c.getParam("size")))
              .addRoute("clear :group", c -> delete.clear(c.getGroup()));

        router.addRoute("set :group :k :v", c -> str.set(c.getGroup(),c.getParam("k"),c.getParam("v")))
              .addRoute("get :group :k", c -> str.get(c.getGroup(),c.getParam("k")));


    }


    public static void main(String[] args) throws IOException {

        CommandQueue commandQueue = new CommandQueue();
        commandQueue.consume();

        ServerSocket serverSocket = new ServerSocket(8081);

        while (true) {
            // ?????????server
            Socket socket = serverSocket.accept();
            System.out.println(socket.getInetAddress() + ":" + socket.getPort() + "??????");
            poolExecutor.submit(() -> task(commandQueue, socket));

        }


    }

    private static void task(CommandQueue commandQueue, Socket socket) {
        RESPContext context = null;

        System.out.println("??????"+Thread.currentThread().getId()+" ??????");
        try {
            while (true) {
                context = new RESPContext();
                context.initServer(socket,new RESPRequest(socket),new RESPResponse(socket));

                Object requestData = null;
                try {
                    // ????????????
                    String res = context.parseRESPBytes();
                    if(res == null){
                        return;
                    }
                    System.out.printf("%s => %s%n", "????????????", res.replace("\r\n", "\\r\\n"));
                    requestData = context.parseRESPString(res);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    context.error(e.getMessage());
                    continue;
                }

                List<String> commandStr = (List<String>) requestData;

                System.out.println("?????????" + socket.getInetAddress() + ":" + socket.getPort() +"?????????");

                // ????????????
                Router.Route route = router.getRoute(String.join(" ",commandStr));
                if (route == null) {
                    context.ok("???????????????????????????");
                    continue;
                }

                Map<String, String> paramMap = route.getMap();
                String name = paramMap.get("group");
                if(name != null){
                    if(groupManager.getGroup(name) == null){
                        context.ok("???group?????????");
                        continue;
                    }
                    context.setGroup(groupManager.getGroup(name));
                }
                context.setRoute(route);


                // ????????????????????????
                commandQueue.add(context);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
    }

}
