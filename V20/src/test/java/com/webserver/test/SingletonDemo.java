package com.webserver.test;

/**单例模式*/


public class SingletonDemo {
    public static void main(String[] args) {
        Singleton s1=  Singleton.getInstance();
        Singleton s2= Singleton.getInstance();

        System.out.println("s1"+s1);
        System.out.println("s2"+s2);
    }
}
