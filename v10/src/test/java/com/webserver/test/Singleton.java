package com.webserver.test;

/**
 * 单例模式
 * 1:私有化构造器
 * 2:定义私有的静态的当前类型的属性
 * 3:提供公开的静态的返回当前类型实例的方法,返回的就是该静态属性.
 *
 */

public class Singleton {
    //2
    private static Singleton instance=new Singleton();


    //1构造器私有化
    private Singleton(){}

    //3
    public static Singleton getInstance(){
        return instance;

    }

}
