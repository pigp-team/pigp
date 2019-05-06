package com.feng.pigp.endecode;

import org.msgpack.annotation.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author feng
 * @date 2019/5/6 11:13
 * @since 1.0
 */
public class JavaCode {

    public static void main(String[] args) {

        Student student = new Student().buildUserName("zhangsan").buildUid(10);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(student);
            objectOutputStream.flush();
            objectOutputStream.close();
            byte[] b = outputStream.toByteArray();
            System.out.println("java seri length " + b.length);
            System.out.println("custom byte length " + student.code().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Message
    public static class Student{

        private String userName;
        private int uid;

        public byte[] code(){
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] value = this.userName.getBytes();
            buffer.putInt(value.length);
            buffer.put(value);
            buffer.putInt(this.uid);
            buffer.flip();
            byte[] result = new byte[buffer.remaining()];
            buffer.get(result);
            return result;
        }

        public Student buildUserName(String userName){

            this.userName = userName;
            return this;
        }

        public Student buildUid(int uid){
            this.uid = uid;
            return this;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "userName='" + userName + '\'' +
                    ", uid=" + uid +
                    '}';
        }
    }
}