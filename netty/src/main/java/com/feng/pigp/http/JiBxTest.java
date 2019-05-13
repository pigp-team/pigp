package com.feng.pigp.http;

import com.feng.pigp.util.GsonUtil;
import org.jibx.runtime.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author feng
 * @date 2019/5/7 20:42
 * @since 1.0
 */
public class JiBxTest {

    public static String encode(Student student) throws JiBXException, IOException {

        IBindingFactory factory = BindingDirectory.getFactory(Student.class);
        StringWriter writer = new StringWriter();
        IMarshallingContext context = factory.createMarshallingContext();
        context.setIndent(2);
        context.marshalDocument(student, "utf-8", null, writer);
        String result = writer.toString();
        writer.close();
        System.out.println("encoder = " + result);
        return result;
    }

    public static Student decode(String str) throws JiBXException {

        IBindingFactory factory = BindingDirectory.getFactory(Student.class);
        StringReader reader = new StringReader(str);
        IUnmarshallingContext context = factory.createUnmarshallingContext();
        Student student = (Student) context.unmarshalDocument(reader);
        return student;
    }

    public static void main(String[] args) throws JiBXException, IOException {

        Student student = new Student();
        student.setId(100);
        student.setUserName("111");

        String xml = encode(student);
        System.out.println(GsonUtil.toJson(decode(xml)));
    }
}