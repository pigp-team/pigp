package com.feng.pigp.endecode;

import com.feng.pigp.util.GsonUtil;
import com.feng.pigp.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @author feng
 * @date 2019/5/6 15:17
 * @since 1.0
 */
public class MessagePackDecode extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        int length = byteBuf.readableBytes();
        byte[] array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
        MessagePack msgPack = new MessagePack();
        list.add(msgPack.read(array));
    }
}