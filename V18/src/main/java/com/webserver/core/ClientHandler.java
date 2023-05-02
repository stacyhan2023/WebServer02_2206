package com.webserver.core;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 与客户端完成一次HTTP的交互
 * 按照HTTP协议要求，与客户端完成一次交互流程为一问一答
 * 因此，这里分为三步完成该工作:
 * 1:解析请求  目的:将浏览器发送的请求内容读取并整理
 * 2:处理请求  目的:根据浏览器的请求进行对应的处理工作
 * 3:发送响应  目的:将服务端的处理结果回馈给浏览器
 *
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run(){
        try {
            //1解析请求,实例化请求对象的过程就是解析的过程
            HttpServletRequest request = new HttpServletRequest(socket);
            HttpServletResponse response=new HttpServletResponse(socket);

            //2处理请求
            DispatcherServlet.getInstance().service(request,response);

            //3发送响应
            response.response();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (EmptyRequestException e) {

        } finally {
            //按照HTTP协议要求,一问一答后断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
