package com.webserver.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**URLDecoderDemo是java提供的api包*/
public class URLDecoderDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {


        String line = "/loginUser?username=%E9%9F%A9%E5%98%89%E6%AC%A3&password=123";
        line = URLDecoder.decode(line, "UTF-8");
        System.out.println(line);

    }
}
