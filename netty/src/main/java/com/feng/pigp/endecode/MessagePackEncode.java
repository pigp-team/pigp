package com.feng.pigp.endecode;

import com.feng.pigp.util.GsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

import java.io.IOException;

/**
 * @author feng
 * @date 2019/5/6 15:23
 * @since 1.0
 */
public class MessagePackEncode extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {

        MessagePack msgPack = new MessagePack();
        byte[] array = msgPack.write(o);
        byteBuf.writeBytes(array);
    }

}