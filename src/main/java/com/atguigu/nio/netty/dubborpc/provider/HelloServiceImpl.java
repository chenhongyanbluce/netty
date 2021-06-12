package com.atguigu.nio.netty.dubborpc.provider;

import com.atguigu.nio.netty.dubborpc.publicinterface.HelloService;

/**
 * @description:
 * @author:chenhongyan
 * @date:2021/6/6 11:01
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息=" + msg);
        // 根据msg返回不同的结果
        if (msg != null) {
            return "你好客户端,我已经收到你的消息[" + msg + "]";
        } else {
            return "你好客户端,我已经收到你的消息";
        }
    }

}
