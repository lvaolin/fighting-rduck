package com.dhy.dubbo.framework;

import com.dhy.dubbo.dto.RpcRequest;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 动态代理的实现
 */
public class MyInvocationHandler implements InvocationHandler {
    private String applicationName;
    public MyInvocationHandler(String applicationName){
         this.applicationName = applicationName;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //发起rpc调用
        //准备rpc请求的参数
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setApplicationName(applicationName);
        //获取类名称
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterValues(args);
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setAsync(true);

        Object object = rpcInvoke(rpcRequest);

        //得到结果返回给调用者
        return object;
    }

    /**
     * 发起RPC调用
     * @param rpcRequest
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Object rpcInvoke(RpcRequest rpcRequest) throws IOException, ClassNotFoundException {

        CompletableFuture<Object> resultFuture = CompletableFuture.supplyAsync(
                ()->{
                    //获取提供者主机列表
                    List<String> hostList = HostFactory.getHosts(rpcRequest);
                    //负载均衡
                    String host = LoadBalanceFactory.loadBalance(hostList);

                    String[] split = host.split(":");
                    URL url = new URL();
                    url.setHost(split[0]);
                    url.setPort( Integer.parseInt(split[1]));

                    //获取协议
                    Protocol protocol = ProtocolFactory.getProtocol();
                    Object result = protocol.send(url, rpcRequest);
                    return result;
                }
        );

        if (rpcRequest.getAsync()) {
            //异步执行 将 future放到当前线程上下文中，便于用户获取
            System.out.println("----异步请求----");
            RpcContext.set(resultFuture);
            return null;
        }else{
            try {
                System.out.println("----同步请求----");
                return resultFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }

    }




}
