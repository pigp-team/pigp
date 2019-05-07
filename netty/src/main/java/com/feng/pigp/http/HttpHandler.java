package com.feng.pigp.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author feng
 * @date 2019/5/7 10:15
 * @since 1.0
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {

        //接收到请求
        //判断是否解析成功
        if(!fullHttpRequest.decoderResult().isSuccess()){
            sendError(channelHandlerContext);
            return;
        }

        if(fullHttpRequest.method() != HttpMethod.GET){
            sendError(channelHandlerContext);
            return;
        }

        String uri = fullHttpRequest.uri();
        if(StringUtils.isEmpty(uri)){
            sendError(channelHandlerContext);
            return;
        }

        String path = parseURI(uri);
        if(StringUtils.isEmpty(path)){
            sendError(channelHandlerContext);
            return;
        }

        File file = new File(path);
        if(file==null || file.isHidden() || !file.exists()){
            sendError(channelHandlerContext);
            return;
        }

        if(!file.isFile()){
            sendError(channelHandlerContext);
            return;
        }

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        long length = randomAccessFile.length();

        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().add("Content-Length", String.valueOf(length));
        //response.headers().add("");




    }

    private String parseURI(String uri) {

        return null;
    }

    private void sendError(ChannelHandlerContext channelHandlerContext) {
    }
}