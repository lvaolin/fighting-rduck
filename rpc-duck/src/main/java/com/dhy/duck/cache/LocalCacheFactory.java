package com.dhy.duck.cache;

import com.dhy.duck.config.DuckConfig;
import com.dhy.duck.register.zookeeper.zkutil.MyZkClient;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;

import java.util.List;

/**
 * @Title LocalCacheFactory
 * @Description  缓存
 * @Author lvaolin
 * @Date 2021/3/14 17:38
 **/
public class LocalCacheFactory {
    /**
     * 提供者地址缓存
     */
    public static Cache<String, List<String>> hostsCache = CacheBuilder.newBuilder().build();
    /**
     * zookeeper 客户端长连接
     */
    public static MyZkClient myZkClient = new MyZkClient(DuckConfig.getInstance());

    /**
     * 目录监视器缓存
     */
    public static Cache<String, PathChildrenCache> pathChildrenCacheCache = CacheBuilder.newBuilder().build();
}
