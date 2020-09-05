# part14

分为了如下几个部分：

* 单体应用锁—— 对应的项目示例是`distribute-demo`项目。
* Redis和Zookeeper自己实现的分布式锁——项目示例`distribute-lock`项目。
* Zookeeper的Curator客户端的分布式锁——项目示例`distribute-zk-lock`项目。
* Redis的Redisson客户端分布式锁——对应项目`redisson-lock`。
* 扣减库存时整合了分布式锁。

