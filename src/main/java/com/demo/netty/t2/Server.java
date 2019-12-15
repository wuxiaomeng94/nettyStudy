package com.demo.netty.t2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Server {

    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void startServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);

        try {
            ServerBootstrap boot = new ServerBootstrap();
            ChannelFuture future = boot.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //System.out.println(Thread.currentThread().getId());

                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ServerChildHandler());
                        }
                    }).bind(8888).sync();

            //System.out.println("server start!");
            ServerFrame.INSTANCE.updateServerMsg("server start!");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }

}

class ServerChildHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = null;
        int oldCount = 0;
        try {
            buf = (ByteBuf) msg;
            oldCount = buf.refCnt();
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            String str = new String(bytes);
            ServerFrame.INSTANCE.updateClientMsg(str);
            System.out.println(str);
            if ("exit".equals(str)) {
                //System.out.println("客户端退出");
                ServerFrame.INSTANCE.updateClientMsg("客户端退出");
                Server.clients.remove(ctx.channel());
                ctx.close();
            }
            Server.clients.writeAndFlush(msg);
        } finally {
            /*if (buf != null) {
                ReferenceCountUtil.release(buf);
            }*/
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //删除出现异常的channel，并关闭连接
        Server.clients.remove(ctx.channel());
        ctx.close();
    }
}

