package com.dhy.dubbo.register.zookeeper.zkutil;

import com.dhy.dubbo.cache.LocalCacheFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.stream.Collectors;

public class MyZkClient {
    //会话超时时间
    private static final int SESSION_TIMEOUT = 30 * 1000;
    //连接超时时间
    private static final int CONNECTION_TIMEOUT = 3 * 1000;
    //ZooKeeper服务地址
    private static final String CONNECT_ADDR = "127.0.0.1:2181";

    //创建连接实例
    private CuratorFramework client = null;


    public MyZkClient() {
        //1 重试策略：初试时间为1s 重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        //2 通过工厂创建连接
        client = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_ADDR).connectionTimeoutMs(CONNECTION_TIMEOUT)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(retryPolicy)
                //命名空间           .namespace("super")
                .build();
        //3 开启连接
        client.start();
    }

    /**
     * //创建永久节点
     * @param path
     * @param data
     */
    public void createNode(String path, String data) {
        try {
            client.create().forPath(path, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建临时节点
     * @param path
     * @param data
     */
    public void createEphemeralNode(String path, String data) {
        try {
            client.create()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, "data".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public boolean exist(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            return stat==null?false:true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取子节点列表
     * @param path
     * @return
     */
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取子节点列表  并订阅
     * @param path
     * @return
     */
    public void watchChildren(String path) {
        try {
            PathChildrenCache pathChildrenCache = LocalCacheFactory.pathChildrenCacheCache.getIfPresent(path);
            if (pathChildrenCache==null) {
                pathChildrenCache = new PathChildrenCache(client,path,true);
                pathChildrenCache.start();
                PathChildrenCache finalPathChildrenCache = pathChildrenCache;
                pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                        System.out.println("变化:"+pathChildrenCacheEvent.toString());
                        List<ChildData> currentData = finalPathChildrenCache.getCurrentData();
                        for (ChildData currentDatum : currentData) {
                            System.out.println(currentDatum.toString());
                        }
                        List<String> collect = currentData.stream().map((data) -> {
                            int i = data.getPath().lastIndexOf("/");
                            String substring = data.getPath().substring(i+1);
                            System.out.println(substring);
                            return substring;
                        }).collect(Collectors.toList());
                        //更新本地主机列表缓存
                        if (collect==null||collect.size()==0) {
                            LocalCacheFactory.hostsCache.invalidate(path);
                        }else{
                            LocalCacheFactory.hostsCache.put(path,collect);
                        }

                    }
                });
                //监视器保存一下
                LocalCacheFactory.pathChildrenCacheCache.put(path,pathChildrenCache);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取节点上的数据
     * @param path
     * @return
     */
    public String getData(String path) {
        try {
            return new String(client.getData().forPath(path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        client.close();
    }

    private void test() throws Exception {
        System.out.println(ZooKeeper.States.CONNECTED);
        System.out.println(client.getState());

        //创建永久节点
        client.create().forPath("/curator", "data".getBytes());
        //创建永久有序节点
        client.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath("/curator_sequential", "data".getBytes());

        //创建临时节点
        client.create().withMode(CreateMode.EPHEMERAL)
                .forPath("/curator/ephemeral", "data".getBytes());

        //创建临时有序节点
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/curator/ephemeral_path1", "data".getBytes());

        client.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath("/curator/ephemeral_path2", "data".getBytes());

        //测试检查某个节点是否存在
        Stat stat1 = client.checkExists().forPath("/curator");
        Stat stat2 = client.checkExists().forPath("/curator2");


        System.out.println("'/curator'是否存在： " + (stat1 != null ? true : false));
        System.out.println("'/curator2'是否存在： " + (stat2 != null ? true : false));

        //获取某个节点的所有子节点
        System.out.println(client.getChildren().forPath("/"));

        //获取某个节点数据
        System.out.println(new String(client.getData().forPath("/curator")));

        //设置某个节点数据
        client.setData().forPath("/curator", "modified data".getBytes());

//        //创建测试节点
        client.create().orSetData().creatingParentContainersIfNeeded()
                .forPath("/curator/del_key1", "/curator/del_key1 data".getBytes());

        client.create().orSetData().creatingParentContainersIfNeeded()
                .forPath("/curator/del_key2", "/curator/del_key2 data".getBytes());


        client.create().forPath("/curator/del_key2/test_key", "test_key data".getBytes());

        //删除该节点
        client.delete().forPath("/curator/del_key1");

        //级联删除子节点
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/curator/del_key2");


        client.close();
    }
}
