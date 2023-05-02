package com.webserver.http;

import com.sun.net.httpserver.Headers;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**响应对象*/
public class HttpServletResponse {

    private Socket socket;

    //状态行相关信息
    private int statusCode=200;
    private String statusReason="OK";

    //响应头相关信息
    private Map<String ,String> headers =new HashMap<>();

    //响应正文相关信息
    private File contentFile;//正文对应的实体文件
    //动态数据可以先通过该流写出大片其内部维护的字节数据中，发送响应时将该数据内容作为正文
    private ByteArrayOutputStream out;



    public HttpServletResponse(Socket socket){
        this.socket=socket;
    }

    /**将当前响应对象内容按照标准的响应格式发送给客户端*/
    public void response() throws IOException {
        //发送前的准备工作
        sendBefore();
        //发送状态行
        sendStatusLine();
        //发送响应头
        sendHeaders();
        //发送响应正文
        sendContent();
    }

    //发送前的准备工作
    private void sendBefore(){
        if(out!=null){//说明有动态数据
            //根据动态数据长度添加响应头Content-Length
            addHeader("Content-Length",out.size()+"");

        }
    }

    //发送状态行
    private void sendStatusLine() throws IOException {
        println("HTTP/1.1"+" "+statusCode+" "+statusReason);
    }
    //发送响应头
    private void sendHeaders() throws IOException {
        Set<Map.Entry<String, String>> entrySet = headers.entrySet();
        for (Map.Entry<String, String> e : entrySet) {
            String name = e.getKey();
            String value = e.getValue();
            println(name + ": " + value);
        }
        //单独发送个回车+换行表示响应头发送完毕
        println("");
    }
    //发送响应正文
    private void sendContent() throws IOException {
        OutputStream out = socket.getOutputStream();
        if(this.out!=null){
            byte[]data=this.out.toByteArray();
            out.write(data);//将动态数据作为正文发送给浏览器
        }else if (contentFile!=null) {
            FileInputStream fis = new FileInputStream(contentFile);
            byte[] buf = new byte[1024 * 10];//10kb
            int len = 0;//记录每次实际读取的字节数
            while ((len = fis.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        }
    }



    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;

        try {
            String contentType= Files.probeContentType(contentFile.toPath());
            if(contentType!=null){
                addHeader("Content-Type",contentType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        addHeader("Content-Length",contentFile.length()+"");

    }

    /**添加一个响应头*/
    public void addHeader(String name,String value){
        headers.put(name,value);
    }

    /**发送重定向响应
     * @param path */
    public void sendRedirect(String path){
        //重定向的状态代码为302
        statusCode=302;
        statusReason="Moved Temporarily";
        //响应头Location
        addHeader("Location",path);
    }

    /**获取字节输出流，通过这个流写出的所有字节最终都会作为响应正文发送给客户端
     * @return
     * */
    private OutputStream getOutputStream(){
        if(out==null){
            out=new ByteArrayOutputStream();
        }
        return out;
    }

    public PrintWriter getWriter(){
        return new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(
                                getOutputStream(),
                                StandardCharsets.UTF_8
                        )
                ),true
        );

    }

    /**添加响应头content-type
     * @param mime */
    public void setContentType(String mime){//text/html
        addHeader("Content-Type",mime);

    }


}


