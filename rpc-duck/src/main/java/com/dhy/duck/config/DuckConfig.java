package com.dhy.duck.config;

import lombok.Data;

/**
 * @Project rduck
 * @Description 配置信息
 * @Author lvaolin
 * @Date 2022/8/28 下午9:15
 */
@Data
public class DuckConfig {

    private static DuckConfig duckConfig  = new DuckConfig();

    private String registerHost;
    private String registerPort;
    //会话超时时间
    private int sessionTimeout = 30 * 1000;
    //连接超时时间
    private int connectionTimeout = 3 * 1000;

    private DuckConfig(){
        this.registerHost = "127.0.0.1";
        this.registerPort = "2181";
    }

    public static DuckConfig getInstance(){
        return  duckConfig;
    }

}
