package com.webserver.test;

import qrcode.QRCodeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class QRDemo {
    public static void main(String[] args) throws Exception {
        String line="你是小猪猪！民民是小猪猪笨笨猪！";
//        QRCodeUtil.encode(line,"./qr.jpg");

        //1内容，2logo图片位置，3二维码生成位置，4logo图片是否压缩尺寸在qr中央区
//        QRCodeUtil.encode(line,"./bbq.jpeg","./qr.jpg",true);
        //1内容，2logo图片位置，3将生成的二维码通过指定的输出流写出，4logo图片是否压缩尺寸在qr中央区
        OutputStream out=new FileOutputStream("./qr.jpg");
        QRCodeUtil.encode(line,"./bbq.jpeg",out,true) ;
        System.out.println("二维码生成完毕");

    }
}
