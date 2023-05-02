package com.webserver.controller;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import qrcode.QRCodeUtil;

import java.io.OutputStream;

@Controller
public class ToolsController {

    @RequestMapping("/createQR")
    public void create(HttpServletRequest request, HttpServletResponse response){

        String line=request.getParameters("content");
        System.out.println("二维码内容:"+line);
        OutputStream out =response.getOutputStream();
        try {
            response.setContentType("image/jpeg");
            QRCodeUtil.encode(line,"./bbq.jpeg",out,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
