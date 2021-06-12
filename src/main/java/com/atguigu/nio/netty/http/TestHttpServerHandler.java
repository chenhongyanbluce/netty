package com.atguigu.nio.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @description:
 * @author:chenhongyan
 * @date:2021/5/30 11:17
 */

/**
 * 1.SimpleChannelInboundHandler是ChannelInboundHandlerAdapter的子类
 * 2.HttpObject表示客户端和服务器端相互通讯的数据封装成HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 当有读取客户端事件发生的时候
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断msg是不是一个HttpRequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("msg  类型=" + msg.getClass());
            System.out.println("客户端地址:" + ctx.channel().remoteAddress());
            // 回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello 我是服务器", CharsetUtil.UTF_8);
            // 构造一个Http的响应,即HttpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            ctx.writeAndFlush(response);
        }

    }

}
