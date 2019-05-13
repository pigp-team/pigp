package com.feng.pigp.custom_protol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.msgpack.MessagePack;
import java.util.HashMap;
import java.util.Map;

/**
 * @author feng
 * @date 2019/5/13 11:16
 * @since 1.0
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder{

    private static final MessagePack messagePack = new MessagePack();


    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        NettyMessage message = new NettyMessage();
        NettyMessage.Header header = new NettyMessage.Header();
        message.setHeader(header);

        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionID(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());

        //attachment
        int size = in.readInt();
        if(size>0){
            Map<String, Object> attch = new HashMap<>(size);

            for(int i=0; i<size; i++){

                int keySize = in.readInt();
                byte[] keyArray = new byte[keySize];
                in.readBytes(keyArray);
                String key = new String(keyArray, "UTF-8");

                int valueSize = in.readInt();
                byte[] valueArray = new byte[valueSize];
                in.readBytes(valueArray);
                Object object = messagePack.read(valueArray);
                attch.put(key, object);
            }
            header.setAttachment(attch);
        }

        if(in.readableBytes()>4){ //说明存在body

            int bodySize = in.readInt();
            byte[] bytes = new byte[bodySize];
            in.readBytes(bytes);
            message.setBody(messagePack.read(bytes));
        }

        return message;
    }
}