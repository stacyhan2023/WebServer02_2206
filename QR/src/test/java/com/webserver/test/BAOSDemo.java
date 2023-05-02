package com.webserver.test;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**低级字节输出流，数据保存在其内部维护的字节数组中*/
public class BAOSDemo {
    public static void main(String[] args) {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        PrintWriter pw=new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                out,
                                StandardCharsets.UTF_8
                        )
                ),true
        );
        pw.println("hello");
        byte[] data=out.toByteArray();
        System.out.println(data.length);
        System.out.println(Arrays.toString(data));


    }
}
