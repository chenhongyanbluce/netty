package com.atguigu.nio.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description:
 * @author:chenhongyan
 * @date:2021/5/30 11:17
 */
public class TestServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 创建服务器端启动的对象,配置启动参数
            ServerBootstrap bootStrap = new ServerBootstrap();
            // 使用链式编程设置参数
            bootStrap.group(boosGroup, workGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioSocketChannel的通道实现
                    .childHandler(new TestServerInitializer());  //给我们的workGroup的EventLoop对应的管道设置处理器

            System.out.println("....服务器 is ready..");
            // 绑定一个端口并且同步,生成提个ChannleFuture对象
            // 启动服务器
            ChannelFuture cf = bootStrap.bind(6668).sync();

            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
