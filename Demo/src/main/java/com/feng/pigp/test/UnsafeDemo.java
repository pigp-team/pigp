package com.feng.pigp.test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author feng
 * @date 2019/6/25 12:54
 * @since 1.0
 */
public class UnsafeDemo {

    public static void main(String[] args) throws InstantiationException, NoSuchFieldException {

        Unsafe unsafe = ToolsUtil.getUnsafe();

        User unsafeUser = (User) unsafe.allocateInstance(User.class);
        System.out.println(unsafeUser);

        User user = new User("xxx", 10);
        System.out.println(user);

        Field field = unsafeUser.getClass().getDeclaredField("age");
        System.out.println(unsafe.objectFieldOffset(field));

        field = user.getClass().getDeclaredField("age");
        System.out.println(unsafe.objectFieldOffset(field));


    }
}