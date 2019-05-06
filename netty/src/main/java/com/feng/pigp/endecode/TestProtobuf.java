package com.feng.pigp.endecode;

import com.feng.pigp.util.GsonUtil;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author feng
 * @date 2019/5/6 19:14
 * @since 1.0
 */
public class TestProtobuf {

    public static void main(String[] args) {

        SubscribeReqProto.SubscribeReq req = SubscribeReqProto.SubscribeReq.newBuilder()
                .setSubReqId(1)
                .setUserName("hahah")
                .setProductName("hehhe")
                .setAddress("hah").build();

        byte[] array = req.toByteArray();
        System.out.println("array : " + array.length);

        try {
            SubscribeReqProto.SubscribeReq req1 = SubscribeReqProto.SubscribeReq.parseFrom(array);
            System.out.println("req1 = " +  req1.toString());
            System.out.println("result = " + req1.equals(req));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }


}