package com.feng.pigp.test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author feng
 * @date 2019/6/25 12:55
 * @since 1.0
 */
public class ToolsUtil {

    public static Unsafe getUnsafe(){

        // 通过反射得到theUnsafe对应的Field对象
        Field field = null;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            // 设置该Field为可访问
            field.setAccessible(true);
            // 通过Field得到该Field对应的具体对象，传入null是因为该Field为static的
            Unsafe unsafe = (Unsafe) field.get(null);
            return unsafe;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        throw new Error();
    }
}