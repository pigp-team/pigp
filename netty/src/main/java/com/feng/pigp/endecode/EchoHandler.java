package com.feng.pigp.endecode;


import com.feng.pigp.util.GsonUtil;
import com.feng.pigp.util.LogUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feng
 * @date 2019/5/6 15:27
 * @since 1.0
 */
public class EchoHandler extends ChannelHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("request = " + msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtil.getLogger().error("channel is exception", cause);
        ctx.close();
    }
}