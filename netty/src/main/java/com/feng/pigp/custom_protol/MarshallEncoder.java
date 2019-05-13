package com.feng.pigp.custom_protol;

import com.feng.pigp.endecode.MarshallingCodeCFactory;
import com.feng.pigp.endecode.MessagePackEncode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import org.msgpack.MessagePack;

import java.io.IOException;

/**
 * @author feng
 * @date 2019/5/13 11:28
 * @since 1.0
 */
public class MarshallEncoder{

    private static final byte[] LEGNTH_PLACEHOLDER = new byte[4]; //长度占位符
    MarshallingEncoder marshaller;

    public MarshallEncoder() {
        this.marshaller = MarshallingCodeCFactory.getMarshallingEncoder();
    }

    protected void encode(Object msg, ByteBuf out) throws IOException {

        MessagePack messagePack = new MessagePack();
        byte[] array = messagePack.write(msg);
        out.writeInt(array.length);
        out.writeBytes(array);
    }
}