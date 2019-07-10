package com.feng.pigp;

/**
 * Hello world!
 *
 */
public class App{


    public static void main( String[] args ) {
        System.out.println(Math.abs("yxst2796".hashCode())%64);
    }

}

abstract  class A{

    public int i = 10;

    public abstract void set(int dex);
}

abstract  class B extends A{

    public abstract int set1(int dex);
}
