package com.atguigu.nio.netty.dubborpc.provider;

/**
 * @description:
 * @author:chenhongyan
 * @date:2021/6/6 11:04
 */

import com.atguigu.nio.netty.dubborpc.netty.NettyServer;

/**
 * 启动一个服务的提供者
 */
public class ServerBootStrap0 {

    public static void main(String[] args) {
        // 代码代填
        NettyServer.startSever("127.0.0.1", 7000);

    }
}
