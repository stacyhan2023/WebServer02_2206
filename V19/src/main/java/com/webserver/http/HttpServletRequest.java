package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequest {
    private Socket socket;
    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议版本

    private String requestURI;//抽象路径中的请求部分
    private String queryString; //抽象部分中的参数部分
    private Map<String,String> parameters =new HashMap<>();//保存每一组参数

    //消息头相关信息
    private Map<String,String> headers = new HashMap<>();

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
        this.socket = socket;
        //1解析请求行
        parseRequestLine();
        //2解析消息头
        parseHeaders();
        //3解析消息正文
        parseContent();
    }
    //解析请求行
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readLine();

        if(line.isEmpty()){//若为空串则是空请求
            throw new EmptyRequestException("request is empty");

        }

        System.out.println("请求行:"+line);
        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];//这里可能出现数组下标越界异常ArrayIndexOutOfBoundsException,原因是浏览器的问题！！！后期我们解决。建议:浏览器测试时尽量不使用后退，前进这样的功能测试。
        protocol = data[2];
        //进一步解析uri
        parseURI();

        System.out.println("method:"+method);
        System.out.println("uri:"+uri);
        System.out.println("protocol:"+protocol);
    }

    //进一步解析uri
    private void parseURI(){
        String[] data= uri.split("\\?");
        requestURI=data[0];
        if(data.length>1){
            queryString=data[1];
           parseParameter(queryString);
        }

        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);

    }

    /**
     * 解析参数
     * 参数的格式应当为：name1=value1&name2=value2...
     * @param line
     * */
    private void parseParameter(String line) {

        try {
            line=URLDecoder.decode(line,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String[] paras=line.split("&");
        for(String para:paras){
            String [] array=para.split("=",2);
            parameters.put(array[0],array[1]);
        }

    }


    //解析消息头
    private void parseHeaders() throws IOException {
        //读取消息头
        while(true) {
            String line = readLine();
            if(line.isEmpty()){
                break;
            }
            System.out.println("消息头:" + line);
            String[] data = line.split(":\\s");
            //key:Connection value:keep-alive
            headers.put(data[0],data[1]);//key:消息头的名字  value:消息头的值

        }
        System.out.println("headers:"+headers);
    }
    //解析消息正文
    private void parseContent() throws IOException {
        //请求方式是否为post请求
        if("post".equalsIgnoreCase(method)){
            if(headers.containsKey("Content-Length")) {
                //根据消息头Content-Length确定正文长度
                int contentLength =Integer.parseInt(
                        headers.get("Content-Length")
                );
                System.out.println("正文长度："+contentLength);
                //读取正文数据
                InputStream in=socket.getInputStream();
                byte[]data=new byte[contentLength];
                in.read(data);

                /**根据content-type来分析正文是什么类型来获取值*/
                String contentType=headers.get("Content-Type");
                if("application/x-www-form-urlencoded".equals(contentType)){//是否为form表单
                    String line=new String(data, StandardCharsets.ISO_8859_1);
                   parseParameter(line);

                }
//                else if(){//比如判断表单提交时是否带附件
//
//                }

            }
        }

    }

    private String readLine() throws IOException {
        //当对同一个socket调用多次getInputStream方法时，获取回来的输入流始终是同一条流
        InputStream in = socket.getInputStream();
        int d;
        StringBuilder builder = new StringBuilder();
        char pre='a',cur='a';
        while((d = in.read())!=-1){
            cur = (char)d;
            if(pre==13&&cur==10){
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }


    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameters(String name) {
        return parameters.get(name);
    }
}
