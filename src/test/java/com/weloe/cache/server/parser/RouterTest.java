package com.weloe.cache.server.parser;

import com.weloe.cache.server.parser.Router;
import com.weloe.cache.server.resp.RESPContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RouterTest {

    @Test
    void newRouter() {
        Router router = new Router();
        router.addRoute("set","set :group :key :v",null);
        router.addRoute("delete","delete :group :key :v",null);
        router.addRoute("expire","expire :group :key :v",null);
        router.addRoute("get","get :group :key :v",null);
        router.addRoute("hash","hget :key :field",null);
        router.addRoute("config","set maxsize :size",null);
        router.addRoute("config","set maxnum :num",null);
        router.addRoute("config","set cachestrategy :strategy",null);

        System.out.println(router);

    }

    @Test
    void parsePattern() {
        Router router = new Router();
        String[] res = router.parsePattern("set :group");
        Assertions.assertArrayEquals(new String[]{"set",":group"},res);
    }

    @Test
    void getRoute() {
        Router router = new Router();

        router.addRoute("command","set :group :key :v",null);
        router.addRoute("command","delete :group :key :v",null);
        router.addRoute("command","expire :group :key :v",null);
        router.addRoute("command","get :group :key :v",null);
        router.addRoute("command","hget :key :field",null);
        router.addRoute("command","config set maxsize :size",null);
        router.addRoute("command","config set maxnum :num",null);
        router.addRoute("command","config set cachestrategy :strategy",null);

        Router.Route route = router.getRoute("command", "delete g1 k1 v1");
        Assertions.assertEquals(route.getNode().getPattern(),"delete :group :key :v");
        Assertions.assertEquals(route.getMap().get("key"),"k1");
    }

    @Test
    void routeHandle(){
        Router router = new Router();

        router.addRoute("group add :name", c -> c.getParam("name"));

        router.addRoute("config set maxByteSize :group :size", c -> 1)
                .addRoute("config get maxByteSize :group", c -> 2)
                .addRoute("config get normalSize :group", c -> 3)
                .addRoute("config set maxNum :group :num", c -> 4);


        router.addRoute("expire :group :k :n", c -> 5)
                .addRoute("ttl :group :k", c -> 6);

        router.addRoute("delete :group :k", c -> 7)
                .addRoute("clear :group", c -> 9);

        router.addRoute("set :group :k :v", c -> c.getParam("k"))
                .addRoute("get :group :k", c -> 10);

        RESPContext context = new RESPContext();
        Router.Route route = router.getRoute("group add 1");

        context.setRoute(route);
        System.out.println(context.getRoute().handle(context));

        RESPContext context1 = new RESPContext();
        Router.Route route1 = router.getRoute("set 1 k v");
        context1.setRoute(route1);
        System.out.println(context1.getRoute().handle(context));

    }
}