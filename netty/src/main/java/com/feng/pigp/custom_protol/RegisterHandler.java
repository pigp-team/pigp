package com.feng.pigp.custom_protol;

import com.feng.pigp.util.LogUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feng
 * @date 2019/7/8 17:22
 * @since 1.0
 */
public class RegisterHandler extends ChannelHandlerAdapter{


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LogUtil.getLogger().info("registered ===================");
    }
}