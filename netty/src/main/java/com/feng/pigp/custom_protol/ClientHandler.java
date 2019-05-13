package com.feng.pigp.custom_protol;

import com.feng.pigp.util.GsonUtil;
import com.feng.pigp.util.LogUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feng
 * @date 2019/5/13 13:04
 * @since 1.0
 */
public class ClientHandler extends ChannelHandlerAdapter{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        LogUtil.getLogger().info("connection success");
        NettyMessage message = new NettyMessage();
        NettyMessage.Header header = new NettyMessage.Header();
        message.setHeader(header);
        message.setBody("hhahah");
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        LogUtil.getLogger().info("msg = {}", GsonUtil.toJson((NettyMessage)msg));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtil.getLogger().error("client connection is error", cause);
        ctx.close();
    }
}