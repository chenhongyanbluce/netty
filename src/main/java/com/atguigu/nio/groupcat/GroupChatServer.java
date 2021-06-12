package com.atguigu.nio.groupcat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @description:
 * @author:chenhongyan
 * @date:2021/5/23 9:58
 */
public class GroupChatServer {

    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    // 构造器,初始化工作
    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    // 监听
    public void listen() {
        try {
            // 循环处理
            while (true) {
                int select = selector.select();
                if (select > 0) {
                    //有时间需要处理
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        // 监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            // 将该sc注册到selector
                            sc.register(selector, SelectionKey.OP_READ);
                            // 提示
                            System.out.println(sc.getRemoteAddress() + " 上线 ");
                        }
                        // 通道发生read事件,即通道是刻可读的状态
                        if (key.isReadable()) {
                            // 处理读
                            readData(key);
                        }
                        // 当前的key删除,防止重复处理
                        iterator.remove();
                    }
                } else {
                    // System.out.println("等待中.....");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {

        }
    }

    private void readData(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                // 输出该消息
                System.out.println("from 客户端:" + msg);
                // 向其他的客户端转发消息(去掉自己),专门写一个方法 // TODO
                sendInfoToOtherClients(msg, channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了..");
                key.cancel();
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    // 转发消息给其他客户
    public void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中");
        //遍历,所有注册到selector上的SocketChannel,并排除self
        for (SelectionKey key : selector.keys()) {
            // 通过key取出对应的SocketChannel
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }


    public static void main(String[] args) {
        // 创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
