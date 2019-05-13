package com.feng.pigp.custom_protol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.msgpack.MessagePack;

import java.util.List;
import java.util.Map;

/**
 * @author feng
 * @date 2019/5/13 11:16
 * @since 1.0
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    private static final MessagePack messagePack = new MessagePack();

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {

        if(msg==null || msg.getHeader()==null){
            throw new Exception("head is empty");
        }

        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeInt(msg.getHeader().getAttachment().size());

        String key;
        byte[] keyArray;
        Object value;
        byte[] valueArray;

        for(Map.Entry<String, Object> entry : msg.getHeader().getAttachment().entrySet()){

            key = entry.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = entry.getValue();
            valueArray = messagePack.write(value);
            sendBuf.writeInt(valueArray.length);
            sendBuf.writeBytes(valueArray);
        }

        if(msg.getBody() != null){

            valueArray = messagePack.write(msg.getBody());
            sendBuf.writeInt(valueArray.length);
            sendBuf.writeBytes(valueArray);
        }else{
            sendBuf.writeInt(0);
        }

        sendBuf.setInt(4, sendBuf.readableBytes());
        out.add(sendBuf);
    }
}