package com.atguigu.nio.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description:
 * @author:chenhongyan
 * @date:2021/5/23 17:52
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {
        // 创建BossGroup和WorkerGroup
        // 说明:1.创建两个线程组,bossGroup和workerGroup
        //      2.bossGroup只是处理连接请求,真正和客户端业务处理,会交给workerGroup完成
        //      3.两个都是无限循环
        //      4.bossgroup和workgroup的子线程(NioEventLoop)的个数
        // 默认cpu的核数*2
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 创建服务器端启动的对象,配置启动参数
            ServerBootstrap bootStrap = new ServerBootstrap();
            // 使用链式编程设置参数
            bootStrap.group(boosGroup, workGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用NioSocketChannel的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)// 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道初始化对象
                        // 给pipeLine设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());

                        }
                    });  //给我们的workGroup的EventLoop对应的管道设置处理器
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
