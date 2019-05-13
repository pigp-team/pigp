package com.feng.pigp.custom_protol;

import com.feng.pigp.util.GsonUtil;
import com.feng.pigp.util.LogUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feng
 * @date 2019/5/13 12:57
 * @since 1.0
 */
public class ServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        NettyMessage message = (NettyMessage)msg;
        LogUtil.getLogger().info("request={}", GsonUtil.toJson(message));
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtil.getLogger().error("connection is error", cause);
        ctx.close();
    }
}