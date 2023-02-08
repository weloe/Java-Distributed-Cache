# Java-Distributed-Cache
用Java写一个分布式缓存





支持命令如下

`[]`内为自定义参数，输入不需要加`[]`

例如输入`group add testGroup`对应`group add [name]`

Group操作

```
group add [name]
```

Config操作

```
config set maxByteSize [group] [size]
config get maxByteSize [group]
config get normalSize [group]
config set maxNum [group] [num]
```

过期时间操作

```
expire [group] [k] [n]
ttl [group] [k]
```

删除操作

```
delete [group] [k]
clear [group]
```

set,get

```
set [group] [k] [v]
get [group] [k]
```

## 使用

启动服务端 src/main/java/com/weloe/cache/Launch.java

启动客户端 src/main/java/com/weloe/cache/client/Client.java

在客户端Client.java窗口输入命令

例如输入

```
group add test
```

输出服务端返回信息

```
ok
```

