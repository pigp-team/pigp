package com.feng.pigp.endecode;

import com.feng.pigp.util.LogUtil;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author feng
 * @date 2019/5/6 16:06
 * @since 1.0
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        /*for(int i=0; i<=10000; i++) {
            JavaCode.Student student = new JavaCode.Student();
            student.buildUid(i).buildUserName("hhaha");
            ctx.write(student);
        }*/

        SubscribeReqProto.SubscribeReq req = SubscribeReqProto.SubscribeReq.newBuilder()
                .setSubReqId(1)
                .setUserName("hahah")
                .setProductName("hehhe")
                .setAddress("hah").build();
        ctx.write(req);
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("response = " + msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtil.getLogger().error("connect is exception", cause);
        ctx.close();
    }
}