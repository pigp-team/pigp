package com.feng.pigp.endecode;

import com.feng.pigp.util.LogUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author feng
 * @date 2019/5/6 15:07
 * @since 1.0
 */
public class Service {

    public static void main(String[] args) {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                       // socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                        socketChannel.pipeline().addLast(new MessagePackDecode());
                        socketChannel.pipeline().addLast(new LengthFieldPrepender(2));
                        socketChannel.pipeline().addLast(new MessagePackEncode());
                        socketChannel.pipeline().addLast(new EchoHandler());

                       //socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                       socketChannel.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
                       socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                       socketChannel.pipeline().addLast(new ProtobufEncoder());
                       socketChannel.pipeline().addLast(new EchoHandler());

                       socketChannel.pipeline().addLast(MarshallingCodeCFactory.getMarshallingDecoder());
                       socketChannel.pipeline().addLast(MarshallingCodeCFactory.getMarshallingEncoder());
                       socketChannel.pipeline().addLast(new EchoHandler());
                    }
                });
        System.out.println("xxx");

        try {
            ChannelFuture future = bootstrap.bind(8080).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LogUtil.getLogger().error("connection is exception", e);
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}