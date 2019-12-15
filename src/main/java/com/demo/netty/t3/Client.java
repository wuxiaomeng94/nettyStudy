package com.demo.netty.t3;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * //这是一个线程池实际上
 */
public class Client {

    private Channel channel = null;

    public void connect() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap boot = new Bootstrap();
        try {
            ChannelFuture future = boot.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer())
                    .connect("localhost", 8888);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        System.out.println("not connected!");
                    } else {
                        System.out.println("connected!");
                        channel = future.channel();
                    }
                }
            });
            future.sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void send(String msg) {
        ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes());
        channel.writeAndFlush(buf);
    }


    public static void main(String[] args) {
        Client c = new Client();
        c.connect();
    }

    public void closeConnect() {
        this.send("exit");
    }


    private static class ClientInstance {
        private static Client INSTANCE = new Client();
    }

    public static Client getInstance() {
        return ClientInstance.INSTANCE;
    }

}

class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast(new TankMsgEncoder())
                .addLast(new ClientHandler());
    }
}

class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = null;
        try {
            buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String str = new String(bytes);
            System.out.println(str);
            ClientFrame.INSTANCE.updateMsg(str);
        } finally {
            if (buf != null) {
                ReferenceCountUtil.release(buf);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //channel第一次连接上可用，会打印出字符串
        /*ByteBuf buf = Unpooled.copiedBuffer("hello world".getBytes());
        ctx.writeAndFlush(buf);*/
        ctx.writeAndFlush(new TankMsg(5, 8));
    }
}


