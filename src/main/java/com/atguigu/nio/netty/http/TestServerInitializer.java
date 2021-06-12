package com.atguigu.nio.netty.http;

import com.atguigu.nio.netty.simple.NettyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * @description:
 * @author:chenhongyan
 * @date:2021/5/30 11:18
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 加入一个netty提供的HttpServerCodec
        pipeline.addLast("myHttpServerCodec",new HttpServerCodec());
        pipeline.addLast("myHttpServerHandler",new TestHttpServerHandler());
//        ch.pipeline().addLast(new NettyServerHandler());
    }
}
