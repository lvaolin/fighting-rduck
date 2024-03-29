package com.dhy.duck.framework;

import java.util.List;
import java.util.Random;

/**
 * 提供者负载均衡
 */
public class LoadBalanceFactory {
    /**
     * 负载均衡
     * @param hostList
     * @return
     */
    public static String loadBalance(List<String> hostList) {
        System.out.println("----负载均衡---");
        if (hostList==null||hostList.size()==0) {
            throw new IllegalArgumentException("没有可用的服务提供者");
        }
        int size = hostList.size();
        Random random = new Random();
        int i = random.nextInt(size);
        return hostList.get(i);
    }
}
