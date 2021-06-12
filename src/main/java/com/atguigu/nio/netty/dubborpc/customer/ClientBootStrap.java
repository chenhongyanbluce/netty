package com.atguigu.nio.netty.dubborpc.customer;

import com.atguigu.nio.netty.dubborpc.netty.NettyClient;
import com.atguigu.nio.netty.dubborpc.publicinterface.HelloService;

/**
 * @description:
 * @author:chenhongyan
 * @date:2021/6/6 14:06
 */
public class ClientBootStrap {

    public static final String provider = "HelloService#hello#";

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        HelloService helloService = (HelloService) nettyClient.getBean(HelloService.class, provider);
        String result = helloService.hello("你好 dubbo");
        System.out.println(result);

    }
}
